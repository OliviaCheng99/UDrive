package com.capstone.udrive.entity.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfoQuery extends BaseParam {

    private String fileId;

    private String fileIdFuzzy;

    private String userId;

    private String userIdFuzzy;

    private String fileMd5;

    private String fileMd5Fuzzy;

    private String filePid;

    private String filePidFuzzy;

    private Long fileSize;

    private String fileName;

    private String fileNameFuzzy;

    private String fileCover;

    private String fileCoverFuzzy;

    private String filePath;

    private String filePathFuzzy;

    private String createTime;

    private String createTimeStart;

    private String createTimeEnd;

    private String lastUpdateTime;

    private String lastUpdateTimeStart;

    private String lastUpdateTimeEnd;

    private Integer folderType;

    private Integer fileCategory;

    private Integer fileType;

    private Integer status;

    private String recoveryTime;

    private String recoveryTimeStart;

    private String recoveryTimeEnd;

    private Integer delFlag;

    private String[] fileIdArray;

    private String[] filePidArray;

    private String[] excludeFileIdArray;

    private Boolean queryExpire;

    private Boolean queryNickName;
}
