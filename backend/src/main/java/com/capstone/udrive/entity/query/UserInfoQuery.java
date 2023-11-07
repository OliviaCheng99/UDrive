package com.capstone.udrive.entity.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoQuery extends BaseParam {

    private String userId;

    private String userIdFuzzy;

    private String nickName;

    private String nickNameFuzzy;

    private String email;

    private String emailFuzzy;

    private String password;

    private String passwordFuzzy;

    private String joinTime;

    private String joinTimeStart;

    private String joinTimeEnd;

    private String lastLoginTime;

    private String lastLoginTimeStart;

    private String lastLoginTimeEnd;

    private Integer status;

    private Long useSpace;

    private Long totalSpace;
}
