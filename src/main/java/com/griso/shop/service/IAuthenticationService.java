package com.griso.shop.service;

import com.griso.shop.model.AuthenticationRequest;
import com.griso.shop.model.AuthenticationResponse;

public interface IAuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
