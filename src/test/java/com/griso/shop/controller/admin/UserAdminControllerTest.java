package com.griso.shop.controller.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.griso.shop.controller.AbstractTest;
import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.UserDB;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.DeleteResponse;
import com.griso.shop.model.User;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@MockBeans({@MockBean(IUserRepo.class)})
class UserAdminControllerTest extends AbstractTest {

    @Autowired
    private IUserRepo userRepoMock;
    @Autowired
    private UserMapper userMapper;

    private static final String BASE_URL = Constants.ENDPOINT.URL_USER_ADMIN;

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @Override
    @BeforeEach
    public void loadUsers() {
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
    void getAllUsers() throws Exception {
        when(userRepoMock.findAll()).thenReturn(userMapper.toUserDBList(userListDto));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/all")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONArray json = new JSONArray(mvcResult.getResponse().getContentAsString());
        List<UserDto> users = mapFromJson(json.toString(), new TypeReference<List<UserDto>>(){});

        assertNotNull(users);
        assertEquals(users.size(), userListDto.size());
        assertEquals(users.get(0), userListDto.get(0));
        assertNotEquals(users.get(0), userListDto.get(1));
    }

    @Test
    void getUserById() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));
        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userMock)));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1234")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        UserDto user = mapFromJson(json.toString(), UserDto.class);

        assertNotNull(user);
        assertEquals(user.getId(), userMock.getId());
        assertEquals(user.getUsername(), userMock.getUsername());
        assertEquals(user.getName(), userMock.getName());
        assertEquals(user.getSurname(), userMock.getSurname());
        assertEquals(user.getBirthday(), userMock.getBirthday());
        assertEquals(user.getPassword(), userMock.getPassword());
        assertEquals(user.getRoles(), userMock.getRoles());
        assertEquals(user.getRoleList(), userMock.getRoleList());
        assertEquals(user.getPermissions(), userMock.getPermissions());
        assertEquals(user.getPermissionList(), userMock.getPermissionList());
    }

    @Test
    void newUser() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));
        User newUser = userList.get(0);

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        UserDto user = mapFromJson(json.toString(), UserDto.class);

        assertNotNull(user);
        assertEquals(user.getId(), userMock.getId());
        assertEquals(user.getUsername(), userMock.getUsername());
        assertEquals(user.getName(), userMock.getName());
        assertEquals(user.getSurname(), userMock.getSurname());
        assertEquals(user.getBirthday(), userMock.getBirthday());
        assertEquals(user.getPassword(), userMock.getPassword());
        assertEquals(user.getRoles(), userMock.getRoles());
        assertEquals(user.getRoleList(), userMock.getRoleList());
        assertEquals(user.getPermissions(), userMock.getPermissions());
        assertEquals(user.getPermissionList(), userMock.getPermissionList());
    }

    @Test
    void newUser_EXISTS() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));
        User newUser = userList.get(0);

        when(userRepoMock.findByUsername(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userListDto.get(0))));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assert(status == 400);
        assertEquals("User registered already", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void newUser_NO_USERNAME() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));
        User newUser = userList.get(0);
        newUser.setUsername(null);

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assert(status == 400);
        assertEquals("Username is required", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void updateUser() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));
        UserDto updateUser = new UserDto(userListDto.get(0));
        updateUser.setPassword("1234");

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userMock)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(updateUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        UserDto user = mapFromJson(json.toString(), UserDto.class);

        assertNotNull(user);
        assertEquals(user.getId(), userMock.getId());
        assertEquals(user.getUsername(), userMock.getUsername());
        assertEquals(user.getName(), userMock.getName());
        assertEquals(user.getSurname(), userMock.getSurname());
        assertEquals(user.getBirthday(), userMock.getBirthday());
        assertNotEquals(user.getPassword(), updateUser.getPassword());
        assertEquals(user.getRoles(), userMock.getRoles());
        assertEquals(user.getRoleList(), userMock.getRoleList());
        assertEquals(user.getPermissions(), userMock.getPermissions());
        assertEquals(user.getPermissionList(), userMock.getPermissionList());
    }

    @Test
    void updateUser_NOT_FOUND() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(0));

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(userMock))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 404);
        assertEquals("User id: 1234 not found", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void deleteUserById() throws Exception {
        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userListDto.get(0))));
        doNothing().when(userRepoMock).deleteById(anyString());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1234")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 200);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        DeleteResponse response = mapFromJson(json.toString(), DeleteResponse.class);

        assertTrue(response.isDeleted());
    }

    @Test
    void deleteUserById_NOT_FOUND() throws Exception {
        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        doNothing().when(userRepoMock).deleteById(anyString());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1234")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert(status == 404);
        assertEquals("User id: 1234 not found", mvcResult.getResponse().getErrorMessage());
    }
}