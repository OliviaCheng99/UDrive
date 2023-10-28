package com.capstone.udrive.entity.enums;

import lombok.Getter;

@Getter
public enum UploadStatusEnums {
    UPLOAD_SECONDS("upload_seconds", "upload seconds"),
    UPLOADING("uploading", "uploading"),
    UPLOAD_FINISH("upload_finish", "upload finish");

    private final String code;
    private final String desc;

    UploadStatusEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
