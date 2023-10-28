package com.capstone.udrive.entity.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileShareQuery extends BaseParam {

    private String shareId;

    private String shareIdFuzzy;

    private String fileId;

    private String fileIdFuzzy;

    private String userId;

    private String userIdFuzzy;

    private Integer validType;

    private String expireTime;

    private String expireTimeStart;

    private String expireTimeEnd;

    private String shareTime;

    private String shareTimeStart;

    private String shareTimeEnd;

    private String code;

    private String codeFuzzy;

    private Integer showCount;

    private Boolean queryFileName;
}
