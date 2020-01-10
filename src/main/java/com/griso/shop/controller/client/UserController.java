package com.griso.shop.controller.client;

import com.griso.shop.dto.UserDto;
import com.griso.shop.model.HTTPException;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller("User")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "Get user data for the authenticated token")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    @GetMapping
    public UserDto getAuthenticatedUser(@AuthenticationPrincipal UserSecurity activeUser) {
        return activeUser.getUser();
    }

    @PostMapping
    @ApiOperation(value = "Create new user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "User registered already", response = HTTPException.class)
    })
    public UserDto saveUser(@RequestBody(required = true) UserDto user) {
        return userService.newUser(user);
    }

}
