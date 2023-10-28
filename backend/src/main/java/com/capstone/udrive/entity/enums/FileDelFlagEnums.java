package com.capstone.udrive.entity.enums;


import lombok.Getter;

@Getter
public enum FileDelFlagEnums {
    DEL(0, "Delete"),
    RECYCLE(1, "Recycle"),
    USING(2, "using");

    private final Integer flag;
    private final String desc;

    FileDelFlagEnums(Integer flag, String desc) {
        this.flag = flag;
        this.desc = desc;
    }

}
