package com.griso.shop.controller.client;

import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.HTTPException;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.service.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Controller("User")
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @ApiOperation(value = "Get user info for the authenticated token",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    public User getAuthenticatedUser(@AuthenticationPrincipal @ApiIgnore UserSecurity activeUser) {
        return userMapper.toUser(activeUser.getUser());
    }

    @PostMapping
    @ApiOperation(value = "Create new user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "User registered already", response = HTTPException.class)
    })
    public void saveUser(@RequestBody(required = true) User user) {
        userService.newUser(user);
    }

    @GetMapping("/validate")
    @ApiIgnore
    public void validateUser(@RequestParam(required = true) String id, @RequestParam(required = true) String key) {
        userService.validateUser(id, key);
    }

    @PutMapping
    @ApiOperation(value = "Update user info for the authenticated token",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    public User updateUser(@AuthenticationPrincipal @ApiIgnore UserSecurity activeUser, @RequestBody User user) {
        return userService.updateUser(activeUser, user);
    }

    @DeleteMapping
    @ApiOperation(value = "Inactivate user account for the authenticated token",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    public void inactivateUser(@AuthenticationPrincipal @ApiIgnore UserSecurity activeUser) {
        userService.inactivateUser(activeUser);
    }
}
