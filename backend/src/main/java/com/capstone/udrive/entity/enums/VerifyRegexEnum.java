package com.capstone.udrive.entity.enums;

import lombok.Getter;

@Getter
public enum VerifyRegexEnum {
    NO("", "No Validation"),
    IP("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}", "IP Address"),
    POSITIVE_INTEGER("^[0-9]*[1-9][0-9]*$", "Positive Integer"),
    COMMON("^\\w+$", "String consisting of numbers, 26 English letters, or underscores"),
    EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", "Email"),
    PHONE("(1[0-9])\\d{9}$", "Mobile Phone Number"),
    PASSWORD("^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{8,}$", "Can only be numbers, letters, special characters 8-18 characters"),
    ACCOUNT("^[0-9a-zA-Z_]{1,}$", "Starts with a letter, composed of numbers, English letters, or underscores"),
    MONEY("^[0-9]+(.[0-9]{1,2})?$", "Amount (Money)");

    private final String regex;
    private final String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

}
