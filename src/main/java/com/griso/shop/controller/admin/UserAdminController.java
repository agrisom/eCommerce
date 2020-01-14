package com.griso.shop.controller.admin;

import com.griso.shop.dto.UserDto;
import com.griso.shop.model.DeleteResponse;
import com.griso.shop.model.HTTPException;
import com.griso.shop.service.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller("UserAdmin")
@RequestMapping("/admin/user")
@Api(tags = "Admin Users")
public class UserAdminController {

    @Autowired
    private IUserService userService;

    @GetMapping
    @ApiOperation(value = "Get users list paginated",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    public Page<UserDto> getUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir) {
        return userService.findUserDtoAll(PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)));
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all users list",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class)
    })
    public List<UserDto> getAllUsers() {
        return userService.findUserDtoAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by ID",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class),
            @ApiResponse(code = 404, message = "User not found for this id {\" + id + \"}\"", response = HTTPException.class)
    })
    public UserDto getUserById(@PathVariable(required = true) String id) {
        return userService.findUserDtoById(id);
    }

    @PostMapping()
    @ApiOperation(value = "New user",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class),
            @ApiResponse(code = 400, message = "Bad request", response = HTTPException.class)
    })
    public UserDto newUser(@RequestBody UserDto userDto) {
        return userService.newUser(userDto);
    }

    @PutMapping()
    @ApiOperation(value = "Update user info",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class),
            @ApiResponse(code = 404, message = "User not found for this id {\" + id + \"}\"", response = HTTPException.class)
    })
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by ID",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = HTTPException.class),
            @ApiResponse(code = 404, message = "User not found for this id {\" + id + \"}\"", response = HTTPException.class)
    })
    public DeleteResponse deleteUserById(@PathVariable(required = true) String id) {
        return userService.deleteUserById(id);
    }
}
