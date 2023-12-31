package com.capstone.udrive.service;

import com.capstone.udrive.entity.dto.SessionWebUserDto;
import com.capstone.udrive.entity.dto.UploadResultDto;
import com.capstone.udrive.entity.po.FileInfo;
import com.capstone.udrive.entity.query.FileInfoQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoService {

    List<FileInfo> findListByParam(FileInfoQuery param);

    Integer findCountByParam(FileInfoQuery param);

    PaginationResultVO<FileInfo> findListByPage(FileInfoQuery param);

    Integer add(FileInfo bean);

    Integer addBatch(List<FileInfo> listBean);

    Integer addOrUpdateBatch(List<FileInfo> listBean);

    FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId);


    Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId);


    Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId);

    UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex,
                               Integer chunks);

    FileInfo rename(String fileId, String userId, String fileName);

    FileInfo newFolder(String filePid, String userId, String folderName);

    void changeFileFolder(String fileIds, String filePid, String userId);

    void removeFile2RecycleBatch(String userId, String fileIds);

    void recoverFileBatch(String userId, String fileIds);

    void delFileBatch(String userId, String fileIds, Boolean adminOp);

    void checkRootFilePid(String rootFilePid, String userId, String fileId);

    void saveShare(String shareRootFilePid, String shareFileIds, String myFolderId, String shareUserId, String cureentUserId);

    Long getUserUseSpace(@Param("userId") String userId);

    void deleteFileByUserId(@Param("userId") String userId);
}