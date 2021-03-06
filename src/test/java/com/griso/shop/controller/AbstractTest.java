package com.griso.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griso.shop.ShopApplication;
import com.griso.shop.controller.admin.UserAdminController;
import com.griso.shop.controller.client.AuthenticationController;
import com.griso.shop.controller.client.UserController;
import com.griso.shop.dto.UserDto;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ShopApplication.class, UserController.class, UserAdminController.class, AuthenticationController.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    protected MockMvc mvc;
    protected ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext context;
    @Autowired
    UserMapper userMapper;

    public List<User> userList = new ArrayList<>();
    public List<UserDto> userListDto = new ArrayList<>();
    public int adminIndex = 0;
    public int clientIndex = 1;

    protected void setUp() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    protected void loadUsers() {
        userList = new ArrayList<>();
        userListDto = new ArrayList<>();

        UserDto user = new UserDto();
        user.setUsername("admin");
        user.setPassword("$2a$10$jqmS7xmy0cLHRGXQyt3Eduaf2ktq9sMeMN8SyZYF5VkM7oJzqdtie");
        user.setName("Admin");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1992, Calendar.JULY, 25);
        user.setBirthday(calendar.getTime());
        user.setSurname("Mock");
        user.setPermissions("TEST");
        user.setRoles("ADMIN;MANAGER");
        user.setActive(true);
        user.setId("1234");
        userList.add(userMapper.toUser(user));
        userListDto.add(user);

        user = new UserDto();
        user.setUsername("albert.griso.mendez@gmail.com");
        user.setPassword("$2a$10$jp.qLe2zGYMblT/U.kEPauJhj6K48ZcaekpGQD5dbx5hS74LLvTfO");
        user.setName("Albert");
        user.setBirthday(calendar.getTime());
        user.setSurname("Griso Mendez");
        user.setPermissions("TEST2");
        user.setRoles("");
        user.setActive(true);
        user.setId("5678");

        userList.add(userMapper.toUser(user));
        userListDto.add(user);
    }

    protected void mockLoggedUser(UserDto user) {
        UserSecurity applicationUser = new UserSecurity(user);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonProcessingException, JsonMappingException, IOException {
        return objectMapper.readValue(json, clazz);
    }

    protected <T> T mapFromJson(String json, TypeReference<T> valueTypeRef)
            throws JsonProcessingException, JsonMappingException, IOException {
        return objectMapper.readValue(json, valueTypeRef);
    }
}
