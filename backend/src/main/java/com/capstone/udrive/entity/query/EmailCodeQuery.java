package com.capstone.udrive.entity.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCodeQuery extends BaseParam {

    private String email;

    private String emailFuzzy;

    private String code;

    private String codeFuzzy;

    private String createTime;

    private String createTimeStart;

    private String createTimeEnd;

    private Integer status;
}
