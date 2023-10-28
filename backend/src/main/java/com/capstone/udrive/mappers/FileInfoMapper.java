package com.capstone.udrive.mappers;

import com.capstone.udrive.entity.po.FileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileInfoMapper<T, P> extends BaseMapper<T, P> {

    Integer updateByFileIdAndUserId(@Param("bean") T t, @Param("fileId") String fileId, @Param("userId") String userId);


    Integer deleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);


    T selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);


    void updateFileStatusWithOldStatus(@Param("fileId") String fileId, @Param("userId") String userId, @Param("bean") T t,
                                       @Param("oldStatus") Integer oldStatus);

    void updateFileDelFlagBatch(@Param("bean") FileInfo fileInfo,
                                @Param("userId") String userId,
                                @Param("filePidList") List<String> filePidList,
                                @Param("fileIdList") List<String> fileIdList,
                                @Param("oldDelFlag") Integer oldDelFlag);


    void delFileBatch(@Param("userId") String userId,
                      @Param("filePidList") List<String> filePidList,
                      @Param("fileIdList") List<String> fileIdList,
                      @Param("oldDelFlag") Integer oldDelFlag);

    Long selectUseSpace(@Param("userId") String userId);

    void deleteFileByUserId(@Param("userId") String userId);
}
