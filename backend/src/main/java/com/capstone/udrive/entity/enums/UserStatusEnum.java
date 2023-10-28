package com.capstone.udrive.entity.enums;


import lombok.Getter;

@Getter
public enum UserStatusEnum {

    DISABLE(0, "Disable"),
    ENABLE(1, "Enable");


    private final Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserStatusEnum getByStatus(Integer status) {
        for (UserStatusEnum item : UserStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
