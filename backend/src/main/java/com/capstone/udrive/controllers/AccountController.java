package com.capstone.udrive.controllers;

import com.capstone.udrive.annotation.GlobalInterceptor;
import com.capstone.udrive.annotation.VerifyParam;
import com.capstone.udrive.component.RedisComponent;
import com.capstone.udrive.constants.Constants;
import com.capstone.udrive.entity.dto.CreateImageCode;
import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.dto.UserSpaceDto;
import com.capstone.udrive.entity.enums.VerifyRegexEnum;
import com.capstone.udrive.entity.po.UserInfo;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.exception.BusinessException;
import com.capstone.udrive.service.EmailCodeService;
import com.capstone.udrive.service.UserInfoService;
import com.capstone.udrive.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Log4j2
@RestController("accountController")
public class AccountController extends CommonController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private RedisComponent redisComponent;

    @GetMapping(value = "/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws
            IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constants.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }


    @PostMapping("/sendEmailCode")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public <T> ResponseVO<T> sendEmailCode(@Validated HttpSession session,
                                           @RequestParam("email") @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                                           @RequestParam("checkCode") @VerifyParam(required = true) String checkCode,
                                           @RequestParam("type") @VerifyParam(required = true) Integer type) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException("Code Fail");
            }
            emailCodeService.sendEmailCode(email, type);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }
    }


    @PostMapping("/register")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public <T> ResponseVO<T> register(@Validated HttpSession session,
                                      @RequestParam("email") @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                                      @RequestParam("nickName") @VerifyParam(required = true, max = 20) String nickName,
                                      @RequestParam("password") @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password,
                                      @RequestParam("checkCode") @VerifyParam(required = true) String checkCode,
                                      @RequestParam("emailCode") @VerifyParam(required = true) String emailCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("In Correct Code");
            }
            userInfoService.register(email, nickName, password, emailCode);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }


    @PostMapping("/login")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public ResponseVO<SessionWebUserDto> login(@Validated HttpSession session, HttpServletRequest request,
                                               @RequestParam("email") @VerifyParam(required = true) String email,
                                               @RequestParam("password") @VerifyParam(required = true) String password,
                                               @RequestParam("checkCode") @VerifyParam(required = true) String checkCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("Incorrect Code");
            }
            SessionWebUserDto sessionWebUserDto = userInfoService.login(email, password);
            session.setAttribute(Constants.SESSION_KEY, sessionWebUserDto);
            return getSuccessResponseVO(sessionWebUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @PostMapping("/resetPwd")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public <T> ResponseVO<T> resetPwd(@Validated HttpSession session,
                                      @RequestParam("email") @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL, max = 150) String email,
                                      @RequestParam("password") @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password,
                                      @RequestParam("checkCode") @VerifyParam(required = true) String checkCode,
                                      @RequestParam("emailCode") @VerifyParam(required = true) String emailCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY))) {
                throw new BusinessException("Incorrect Code");
            }
            userInfoService.resetPwd(email, password, emailCode);
            return getSuccessResponseVO(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

    @GetMapping("/getUserInfo")
    @GlobalInterceptor
    public ResponseVO<SessionWebUserDto> getUserInfo(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
        return getSuccessResponseVO(sessionWebUserDto);
    }

    @PostMapping("/getUseSpace")
    @GlobalInterceptor
    public ResponseVO<UserSpaceDto> getUseSpace(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
        return getSuccessResponseVO(redisComponent.getUserSpaceUse(sessionWebUserDto.getUserId()));
    }

    @PostMapping("/logout")
    public <T> ResponseVO<T> logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }

    @PostMapping("/updatePassword")
    @GlobalInterceptor(checkParams = true)
    public <T> ResponseVO<T> updatePassword(@Validated HttpSession session,
                                            @RequestParam("password") @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD, min = 8, max = 18) String password) {
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(StringTools.encodeByMD5(password));
        userInfoService.updateUserInfoByUserId(userInfo, sessionWebUserDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
