package com.griso.shop.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document(collection = "user")
public class UserDB {

    @Id
    private String id;

    @NotNull
    @NotBlank
    @NotNull@NotBlank
    @NotNull@NotBlank
    @NotNull
    @NotBlank
    private String username;

    private String password;

    private String name;

    private String surname;

    private Date birthday;

    private boolean active;

    private String roles = "";

    private String permissions = "";

}
