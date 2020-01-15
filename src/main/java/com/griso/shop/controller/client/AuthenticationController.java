package com.griso.shop.controller.client;

import com.griso.shop.model.AuthenticationRequest;
import com.griso.shop.model.AuthenticationResponse;
import com.griso.shop.model.ErrorResponse;
import com.griso.shop.service.IAuthenticationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@Controller("authentication")
@Api(tags = "Authentication")
public class AuthenticationController {

    @Autowired
    private IAuthenticationService authService;

    @ApiOperation(value = "Generate user token by given username and password")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class)
    })
    @PostMapping
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authService.authenticate(authenticationRequest);
    }

}
