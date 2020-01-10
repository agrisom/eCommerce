package com.griso.shop.model;

import lombok.Data;

import java.util.Date;

@Data
public class HTTPException {
    Date timestamp;
    int status;
    String error;
    String message;
    String path;

    public HTTPException() {
    }

}
