package com.griso.shop.controller.client;

import com.griso.shop.model.AuthenticationRequest;
import com.griso.shop.model.AuthenticationResponse;
import com.griso.shop.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@Controller("authorization")
public class AuthorizationController {

    @Autowired
    private IAuthenticationService authService;

    @PostMapping
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authService.authenticate(authenticationRequest);
    }

}
