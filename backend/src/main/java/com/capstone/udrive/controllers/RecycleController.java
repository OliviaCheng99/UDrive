package com.capstone.udrive.controllers;

import com.capstone.udrive.annotation.GlobalInterceptor;
import com.capstone.udrive.annotation.VerifyParam;
import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.enums.FileDelFlagEnums;
import com.capstone.udrive.entity.po.FileInfo;
import com.capstone.udrive.entity.query.FileInfoQuery;
import com.capstone.udrive.entity.vo.FileInfoVO;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.service.FileInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("recycleController")
@RequestMapping("/recycle")
public class RecycleController extends CommonController {

    @Resource
    private FileInfoService fileInfoService;

    @PostMapping("/loadRecycleList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<PaginationResultVO<FileInfoVO>> loadRecycleList(@Validated HttpSession session,
                                                                      @RequestParam("pageNo") Integer pageNo,
                                                                      @RequestParam("pageSize") Integer pageSize) {
        FileInfoQuery query = new FileInfoQuery();
        query.setPageSize(pageSize);
        query.setPageNo(pageNo);
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("recovery_time desc");
        query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
        PaginationResultVO<FileInfo> result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
    }

    @PostMapping("/recoverFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileInfoVO> recoverFile(@Validated HttpSession session,
                                              @RequestParam("fileIds") @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.recoverFileBatch(webUserDto.getUserId(), fileIds);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileInfoVO> delFile(@Validated HttpSession session,
                                          @RequestParam("fileIds") @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.delFileBatch(webUserDto.getUserId(), fileIds, false);
        return getSuccessResponseVO(null);
    }
}
