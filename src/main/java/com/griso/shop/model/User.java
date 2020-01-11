package com.griso.shop.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private String id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private Date birthday;

}
