package com.capstone.udrive.controllers;

import com.capstone.udrive.annotation.GlobalInterceptor;
import com.capstone.udrive.annotation.VerifyParam;
import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.po.FileShare;
import com.capstone.udrive.entity.query.FileShareQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.service.FileShareService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends CommonController {
    @Resource
    private FileShareService fileShareService;


    @PostMapping("/loadShareList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<PaginationResultVO<FileShare>> loadShareList(@Validated HttpSession session,
                                                                   FileShareQuery query) {
        query.setOrderBy("share_time desc");
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        query.setUserId(userDto.getUserId());
        query.setQueryFileName(true);
        PaginationResultVO<FileShare> resultVO = this.fileShareService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

    @PostMapping("/shareFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileShare> shareFile(@Validated HttpSession session,
                                           @RequestParam("fileId") @VerifyParam(required = true) String fileId,
                                           @RequestParam("validType") @VerifyParam(required = true) Integer validType,
                                           String code) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        FileShare share = new FileShare();
        share.setFileId(fileId);
        share.setValidType(validType);
        share.setCode(code);
        share.setUserId(userDto.getUserId());
        fileShareService.saveShare(share);
        return getSuccessResponseVO(share);
    }

    @PostMapping("/cancelShare")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileShare> cancelShare(@Validated HttpSession session,
                                             @RequestParam("shareIds") @VerifyParam(required = true) String shareIds) {
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        fileShareService.deleteFileShareBatch(shareIds.split(","), userDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
