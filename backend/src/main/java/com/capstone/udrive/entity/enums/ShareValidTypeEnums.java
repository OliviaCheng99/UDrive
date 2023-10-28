package com.capstone.udrive.entity.enums;

import lombok.Getter;

@Getter
public enum ShareValidTypeEnums {
    DAY_1(0, 1, "1 day"),
    DAY_7(1, 7, "7 days"),
    DAY_30(2, 30, "30 days"),
    FOREVER(3, -1, "forever");

    private final Integer type;
    private final Integer days;
    private final String desc;

    ShareValidTypeEnums(Integer type, Integer days, String desc) {
        this.type = type;
        this.days = days;
        this.desc = desc;
    }


    public static ShareValidTypeEnums getByType(Integer type) {
        for (ShareValidTypeEnums typeEnums : ShareValidTypeEnums.values()) {
            if (typeEnums.getType().equals(type)) {
                return typeEnums;
            }
        }
        return null;
    }
}
