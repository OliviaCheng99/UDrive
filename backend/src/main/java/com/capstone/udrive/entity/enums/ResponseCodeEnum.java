package com.capstone.udrive.entity.enums;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    CODE_200(200, "Request Succeeded"),
    CODE_404(404, "Request Address Not Found"),
    CODE_500(500, "Server Returned an Error, Please Contact the Administrator"),
    CODE_600(600, "Request Parameter Error"),
    CODE_601(601, "Information Already Exists"),
    CODE_602(602, "Share Link Does Not Exist or Has Expired"),
    CODE_603(603, "Share Verification Expired, Please Revalidate"),
    CODE_604(604, "Insufficient Disk Space, Please Expand"),
    CODE_605(605, "Login Timeout, Please Log in Again");

    private final Integer code;
    private final String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
