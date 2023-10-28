package com.capstone.udrive.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingsDto implements Serializable {
    private String registerEmailTitle = "register code";

    private String registerEmailContent = "You code is %s, valid in 15 mins";

    private Integer userInitUseSpace = 5;
}
