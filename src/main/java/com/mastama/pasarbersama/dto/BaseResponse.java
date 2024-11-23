package com.mastama.pasarbersama.dto;

import lombok.Data;

@Data
public class BaseResponse {
    private Boolean responseStatus;
    private String responseDesc;
    private Object data;
}
