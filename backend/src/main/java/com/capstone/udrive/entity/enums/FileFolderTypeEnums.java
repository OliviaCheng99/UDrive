package com.capstone.udrive.entity.enums;


import lombok.Getter;

@Getter
public enum FileFolderTypeEnums {
    FILE(0, "file"),
    FOLDER(1, "folder");

    private final Integer type;
    private final String desc;

    FileFolderTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
