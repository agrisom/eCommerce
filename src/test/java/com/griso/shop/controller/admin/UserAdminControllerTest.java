package com.griso.shop.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.griso.shop.controller.AbstractTest;
import com.griso.shop.dto.UserDto;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.repository.IUserRepo;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockBeans({@MockBean(IUserRepo.class)})
class UserAdminControllerTest extends AbstractTest {

    @Autowired
    private IUserRepo userRepoMock;
    @Autowired
    private UserMapper userMapper;

    private static final String BASE_URL = "/admin/user";

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
        super.loadUsers();
    }

    @Test
    void getUsers() throws Exception {
        Page<UserDto> mockPage = new PageImpl<>(userListDto);
        when(userRepoMock.findAll(any(Pageable.class))).thenReturn(mockPage.map(userMapper::toUserDB));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        List<UserDto> userPage = mapFromJson(json.getJSONArray("content").toString(), new TypeReference<List<UserDto>>(){});

        assertNotNull(userPage);
        assertEquals(json.getLong("numberOfElements"), mockPage.getTotalElements());
        assertEquals(userPage.size(), userListDto.size());
        assertEquals(userPage.get(0), userListDto.get(0));
        assertNotEquals(userPage.get(0), userListDto.get(1));
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void newUser() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void testDeleteUserById() {
    }
}