package com.griso.shop.controller.client;

import com.griso.shop.model.HTTPException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")
@Controller("foo")
public class FooController {

    @GetMapping
    public String getFoo() {
        return "foo";
    }

    @ApiOperation(value = "Test foo endpoint")
    @GetMapping("/public")
    public String getPublicContent() {
        return "Public";
    }

    @ApiOperation(value = "Test protected endpoint for authenticated users",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 403, message = "Unauthorized", response = HTTPException.class)
    })
    @GetMapping("/private")
    public String getPrivateContent() {
        return "Private (Only logged users)";
    }

    @ApiOperation(value = "Test protected endpoint for ADMIN users",
            authorizations = {@Authorization(value="Bearer")})
    @ApiResponses({
            @ApiResponse(code = 403, message = "Unauthorized", response = HTTPException.class)
    })
    @GetMapping("/admin")
    public String getAdminContent() {
        return "Private (Only ADMIN users)";
    }
}
