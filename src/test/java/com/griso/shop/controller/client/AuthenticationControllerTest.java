package com.griso.shop.controller.client;

import com.griso.shop.controller.AbstractTest;
import com.griso.shop.dto.UserDto;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.AuthenticationRequest;
import com.griso.shop.model.AuthenticationResponse;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.service.IUserService;
import com.griso.shop.util.JwtUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MockBeans({@MockBean(IUserService.class)})
class AuthenticationControllerTest extends AbstractTest {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private static final String BASE_URL = "/authenticate";

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
        super.loadUsers();
    }

    @Test
    void createAuthenticationToken() throws Exception {
        UserDto userMock = userListDto.get(adminIndex);
        when(userService.findUserDtoByUsername(anyString())).thenReturn(userMock);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("admin");
        request.setPassword("admin");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(request))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        AuthenticationResponse response = mapFromJson(json.toString(), AuthenticationResponse.class);

        assertEquals(jwtUtil.extractUsername(response.getToken()), userMock.getUsername());
        assertFalse(jwtUtil.isTokenExpired(response.getToken()));
        assertTrue(jwtUtil.extractExpiration(response.getToken()).after(new Date()));
        assertTrue(jwtUtil.validateToken(response.getToken(), new UserSecurity(userMock)));
    }

    @Test
    void createAuthenticationToken_UNAUTORIZED() throws Exception {
        UserDto userMock = userListDto.get(adminIndex);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(request))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }
}