package com.capstone.udrive.service;

import com.capstone.udrive.entity.dto.SessionShareDto;
import com.capstone.udrive.entity.po.FileShare;
import com.capstone.udrive.entity.query.FileShareQuery;
import com.capstone.udrive.entity.vo.PaginationResultVO;

import java.util.List;


public interface FileShareService {
    List<FileShare> findListByParam(FileShareQuery param);

    Integer findCountByParam(FileShareQuery param);

    PaginationResultVO<FileShare> findListByPage(FileShareQuery param);

    Integer add(FileShare bean);

    Integer addBatch(List<FileShare> listBean);

    Integer addOrUpdateBatch(List<FileShare> listBean);

    FileShare getFileShareByShareId(String shareId);

    Integer updateFileShareByShareId(FileShare bean, String shareId);

    Integer deleteFileShareByShareId(String shareId);

    void saveShare(FileShare share);

    void deleteFileShareBatch(String[] shareIdArray, String userId);

    SessionShareDto checkShareCode(String shareId, String code);
}