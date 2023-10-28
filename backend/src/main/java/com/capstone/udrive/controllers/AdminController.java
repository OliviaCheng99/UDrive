package com.capstone.udrive.controllers;

import com.capstone.udrive.annotation.GlobalInterceptor;
import com.capstone.udrive.annotation.VerifyParam;
import com.capstone.udrive.component.RedisComponent;
import com.capstone.udrive.entity.dto.SysSettingsDto;
import com.capstone.udrive.entity.po.FileInfo;
import com.capstone.udrive.entity.po.UserInfo;
import com.capstone.udrive.entity.query.FileInfoQuery;
import com.capstone.udrive.entity.query.UserInfoQuery;
import com.capstone.udrive.entity.vo.FolderVO;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.entity.vo.UserInfoVO;
import com.capstone.udrive.service.FileInfoService;
import com.capstone.udrive.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("adminController")
@RequestMapping("/admin")
public class AdminController extends CommonFileController {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private FileInfoService fileInfoService;

    @PostMapping("/getSysSettings")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public ResponseVO<SysSettingsDto> getSysSettings() {
        return getSuccessResponseVO(redisComponent.getSysSettingsDto());
    }


    @PostMapping("/saveSysSettings")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public <T> ResponseVO<T> saveSysSettings(
            @VerifyParam(required = true) String registerEmailTitle,
            @VerifyParam(required = true) String registerEmailContent,
            @VerifyParam(required = true) Integer userInitUseSpace) {
        SysSettingsDto sysSettingsDto = new SysSettingsDto();
        sysSettingsDto.setRegisterEmailTitle(registerEmailTitle);
        sysSettingsDto.setRegisterEmailContent(registerEmailContent);
        sysSettingsDto.setUserInitUseSpace(userInitUseSpace);
        redisComponent.saveSysSettingsDto(sysSettingsDto);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/loadUserList")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public ResponseVO<PaginationResultVO<UserInfoVO>> loadUser(UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("join_time desc");
        PaginationResultVO<UserInfo> resultVO = userInfoService.findListByPage(userInfoQuery);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, UserInfoVO.class));
    }


    @PostMapping("/updateUserStatus")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public <T> ResponseVO<T> updateUserStatus(@VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer status) {
        userInfoService.updateUserStatus(userId, status);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/updateUserSpace")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public <T> ResponseVO<T> updateUserSpace(@VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer changeSpace) {
        userInfoService.changeUserSpace(userId, changeSpace);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/loadFileList")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public ResponseVO<PaginationResultVO<FileInfo>> loadDataList(FileInfoQuery query) {
        query.setOrderBy("last_update_time desc");
        query.setQueryNickName(true);
        PaginationResultVO<FileInfo> resultVO = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

    @PostMapping("/getFolderInfo")
    @GlobalInterceptor(checkLogin = false, checkAdmin = true, checkParams = true)
    public ResponseVO<List<FolderVO>> getFolderInfo(@VerifyParam(required = true) String path) {
        return super.getFolderInfo(path, null);
    }


    @GetMapping("/getFile/{userId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public void getFile(HttpServletResponse response,
                        @PathVariable("userId") @VerifyParam(required = true) String userId,
                        @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        super.getFile(response, fileId, userId);
    }


    @GetMapping("/ts/getVideoInfo/{userId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public void getVideoInfo(HttpServletResponse response,
                             @PathVariable("userId") @VerifyParam(required = true) String userId,
                             @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        super.getFile(response, fileId, userId);
    }

    @GetMapping("/createDownloadUrl/{userId}/{fileId}")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public ResponseVO<String> createDownloadUrl(@PathVariable("userId") @VerifyParam(required = true) String userId,
                                                @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        return super.createDownloadUrl(fileId, userId);
    }


    @GetMapping("/download/{code}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("code") @VerifyParam(required = true) String code) throws Exception {
        super.download(request, response, code);
    }


    @PostMapping("/delFile")
    @GlobalInterceptor(checkParams = true, checkAdmin = true)
    public <T> ResponseVO<T> delFile(@VerifyParam(required = true) String fileIdAndUserIds) {
        String[] fileIdAndUserIdArray = fileIdAndUserIds.split(",");
        for (String fileIdAndUserId : fileIdAndUserIdArray) {
            String[] itemArray = fileIdAndUserId.split("_");
            fileInfoService.delFileBatch(itemArray[0], itemArray[1], true);
        }
        return getSuccessResponseVO(null);
    }
}
