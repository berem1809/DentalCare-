package com.medical.doc4all.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class APIResponse<t> {
    private boolean success;
    private int statusCode;
    private String message;
    private t data;

    public APIResponse(boolean success, int status, String message,t data){
        this.success = success;
        this.statusCode = status;
        this.message = message;
        this.data = data;
    }

    public APIResponse(boolean success, int status, String message){
        this(success, status, message, null);
    }
}
