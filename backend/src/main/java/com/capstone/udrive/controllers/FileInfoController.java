package com.capstone.udrive.controllers;

import com.capstone.udrive.annotation.GlobalInterceptor;
import com.capstone.udrive.annotation.VerifyParam;
import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.dto.UploadResultDto;
import com.capstone.udrive.entity.enums.FileCategoryEnums;
import com.capstone.udrive.entity.enums.FileDelFlagEnums;
import com.capstone.udrive.entity.enums.FileFolderTypeEnums;
import com.capstone.udrive.entity.po.FileInfo;
import com.capstone.udrive.entity.query.FileInfoQuery;
import com.capstone.udrive.entity.vo.FileInfoVO;
import com.capstone.udrive.entity.vo.FolderVO;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import com.capstone.udrive.entity.vo.ResponseVO;
import com.capstone.udrive.utils.CopyTools;
import com.capstone.udrive.utils.StringTools;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("fileInfoController")
@RequestMapping("/file")
public class FileInfoController extends CommonFileController {

    @PostMapping("/loadDataList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<PaginationResultVO<FileInfoVO>> loadDataList(@Validated HttpSession session,
                                                                   FileInfoQuery query,
                                                                   String category) {
        FileCategoryEnums categoryEnum = FileCategoryEnums.getByCode(category);
        if (null != categoryEnum) {
            query.setFileCategory(categoryEnum.getCategory());
        }
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("last_update_time desc");
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        PaginationResultVO<FileInfo> result = fileInfoService.findListByPage(query);
        return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
    }

    @PostMapping("/uploadFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<UploadResultDto> uploadFile(@Validated HttpSession session,
                                                  @RequestParam("fileId") String fileId,
                                                  @RequestParam("file") MultipartFile file,
                                                  @RequestParam("fileName") @VerifyParam(required = true) String fileName,
                                                  @RequestParam("filePid") @VerifyParam(required = true) String filePid,
                                                  @RequestParam("fileMd5") @VerifyParam(required = true) String fileMd5,
                                                  @RequestParam("chunkIndex") @VerifyParam(required = true) Integer chunkIndex,
                                                  @RequestParam("chunks") @VerifyParam(required = true) Integer chunks) {

        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        UploadResultDto resultDto = fileInfoService.uploadFile(webUserDto, fileId, file, fileName, filePid, fileMd5, chunkIndex, chunks);
        return getSuccessResponseVO(resultDto);
    }


    @GetMapping("/getImage/{imageFolder}/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName) {
        super.getImage(response, imageFolder, imageName);
    }

    @GetMapping("/ts/getVideoInfo/{fileId}")
    public void getVideoInfo(HttpServletResponse response, @Validated HttpSession session, @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, webUserDto.getUserId());
    }

    @GetMapping("/getFile/{fileId}")
    public void getFile(HttpServletResponse response, @Validated HttpSession session, @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        super.getFile(response, fileId, webUserDto.getUserId());
    }

    @PostMapping("/newFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileInfo> newFolder(@Validated HttpSession session,
                                          @RequestParam("filePid") @VerifyParam(required = true) String filePid,
                                          @RequestParam("fileName") @VerifyParam(required = true) String fileName) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfo fileInfo = fileInfoService.newFolder(filePid, webUserDto.getUserId(), fileName);
        return getSuccessResponseVO(fileInfo);
    }

    @PostMapping("/getFolderInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<List<FolderVO>> getFolderInfo(@Validated HttpSession session, @RequestParam("path") @VerifyParam(required = true) String path) {
        return super.getFolderInfo(path, getUserInfoFromSession(session).getUserId());
    }


    @PostMapping("/rename")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<FileInfoVO> rename(@Validated HttpSession session,
                                         @RequestParam("fileId") @VerifyParam(required = true) String fileId,
                                         @RequestParam("fileName") @VerifyParam(required = true) String fileName) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        FileInfo fileInfo = fileInfoService.rename(fileId, webUserDto.getUserId(), fileName);
        return getSuccessResponseVO(CopyTools.copy(fileInfo, FileInfoVO.class));
    }

    @PostMapping("/loadAllFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<List<FileInfoVO>> loadAllFolder(@Validated HttpSession session,
                                                      @RequestParam("filePid") @VerifyParam(required = true) String filePid,
                                                      @RequestParam("currentFileIds") String currentFileIds) {
        FileInfoQuery query = new FileInfoQuery();
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setFilePid(filePid);
        query.setFolderType(FileFolderTypeEnums.FOLDER.getType());
        if (!StringTools.isEmpty(currentFileIds)) {
            query.setExcludeFileIdArray(currentFileIds.split(","));
        }
        query.setDelFlag(FileDelFlagEnums.USING.getFlag());
        query.setOrderBy("create_time desc");
        List<FileInfo> fileInfoList = fileInfoService.findListByParam(query);
        return getSuccessResponseVO(CopyTools.copyList(fileInfoList, FileInfoVO.class));
    }

    @PostMapping("/changeFileFolder")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<List<FileInfoVO>> changeFileFolder(@Validated HttpSession session,
                                                         @RequestParam("fileIds") @VerifyParam(required = true) String fileIds,
                                                         @RequestParam("filePid") @VerifyParam(required = true) String filePid) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.changeFileFolder(fileIds, filePid, webUserDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @GetMapping("/createDownloadUrl/{fileId}")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<String> createDownloadUrl(@Validated HttpSession session, @PathVariable("fileId") @VerifyParam(required = true) String fileId) {
        return super.createDownloadUrl(fileId, getUserInfoFromSession(session).getUserId());
    }

    @GetMapping("/download/{code}")
    @GlobalInterceptor(checkLogin = false, checkParams = true)
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("code") @VerifyParam(required = true) String code) throws Exception {
        super.download(request, response, code);
    }


    @PostMapping("/delFile")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO<List<FileInfoVO>> delFile(@Validated HttpSession session,
                                                @RequestParam("fileIds") @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.removeFile2RecycleBatch(webUserDto.getUserId(), fileIds);
        return getSuccessResponseVO(null);
    }
}