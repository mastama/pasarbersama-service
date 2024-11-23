package com.mastama.pasarbersama.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private Boolean responseStatus;
    private String responseDesc;
    private T data;
}
