package com.capstone.udrive.entity.enums;

import lombok.Getter;

@Getter
public enum FileStatusEnums {
    TRANSFER(0, "Transfer"),
    TRANSFER_FAIL(1, "Transfer Fail"),
    USING(2, "Using");

    private final Integer status;
    private final String desc;

    FileStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
