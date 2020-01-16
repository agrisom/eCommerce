package com.griso.shop.controller.client;

import com.griso.shop.controller.AbstractTest;
import com.griso.shop.dto.UserDto;
import com.griso.shop.entities.UserDB;
import com.griso.shop.mapper.UserMapper;
import com.griso.shop.model.User;
import com.griso.shop.model.UserSecurity;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.JwtUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MockBeans({@MockBean(IUserRepo.class), @MockBean(JavaMailSender.class)})
class UserControllerTest extends AbstractTest {

    @Autowired
    private IUserRepo userRepoMock;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JavaMailSender javaMailSender;

    private static final String BASE_URL = "/user";

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
    void getAuthenticatedUser() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        User response = mapFromJson(json.toString(), User.class);
        assertEquals(response, userList.get(clientIndex));
    }

    @Test
    void saveUser() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));
        User newUser = userList.get(clientIndex);

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void saveUser_INVALID_MAIL() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));
        User newUser = userList.get(clientIndex);
        newUser.setUsername("albert");

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals("Invalid email address", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void saveUser_INVALID_NAME() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));
        User newUser = userList.get(clientIndex);
        newUser.setName("");

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals("User name is required", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void saveUser_INVALID_PWD() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));
        User newUser = userList.get(clientIndex);
        newUser.setPassword(null);

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        assertEquals("User password is required", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void saveUser_EXISTS() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));
        User newUser = userList.get(clientIndex);

        when(userRepoMock.findByUsername(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userListDto.get(clientIndex))));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(newUser))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(400, status);
        assertEquals("User registered already", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void validateUser() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));
        UserDto user = userListDto.get(clientIndex);
        String token = jwtUtil.generateToken(new UserSecurity(user));

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/validate?id=" + user.getId() + "&key=" + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void validateUser_NOT_FOUND() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));
        UserDto user = userListDto.get(clientIndex);
        String token = jwtUtil.generateToken(new UserSecurity(user));

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/validate?id=" + user.getId() + "&key=" + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals(404, status);
        assertEquals("User not found", response);
    }

    @Test
    void validateUser_TOKEN_EXPIRED() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));
        UserDto user = userListDto.get(clientIndex);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGJlcnQuZ3Jpc28ubWVuZGV6QGdtYWlsLmNvbSIsImV4cCI6MTU3OTE3MjgzOSwiaWF0IjoxNTc5MTcyODM5fQ.K3bhFEyfceFV5eSYC7XsjTf261N9TU7N9tc6AMpO53E";

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/validate?id=" + user.getId() + "&key=" + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals(401, status);
        assertEquals("Time has expired", response);
    }

    @Test
    void validateUser_TOKEN_ERROR() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));
        UserDto user = userListDto.get(clientIndex);
        String token = "";

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/validate?id=" + user.getId() + "&key=" + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals(401, status);
        assertEquals("Invalid token", response);
    }

    @Test
    void updateUser() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));

        UserDto userUpdated = new UserDto(userListDto.get(clientIndex));
        userUpdated.setName("Name 2");
        userUpdated.setSurname("Surname");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1992, Calendar.JULY, 25);
        userUpdated.setBirthday(calendar.getTime());
        userUpdated.setPassword("123");

        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userUpdated));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(userUpdated))
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        User response = mapFromJson(json.toString(), User.class);
        assertEquals(response, userMapper.toUser(userUpdated));
    }

    @Test
    void inactivateUser() throws Exception {
        mockLoggedUser(userListDto.get(clientIndex));

        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userListDto.get(clientIndex)));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void sendEmailResetPassword() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));

        when(userRepoMock.findByUsername(anyString())).thenReturn(Optional.of(userMapper.toUserDB(userMock)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/reset/" + userMock.getUsername())
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void sendEmailResetPassword_NOT_FOUND() throws Exception {
        UserDto userMock = new UserDto(userListDto.get(clientIndex));

        when(userRepoMock.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(userMock));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/reset/" + userMock.getUsername())
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        assertEquals("User " + userMock.getUsername() + " not found", mvcResult.getResponse().getErrorMessage());
    }

    @Test
    void resetUserPassword() throws Exception {
        UserDto user = userListDto.get(clientIndex);
        String token = jwtUtil.generateToken(new UserSecurity(user));

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/reset")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", user.getId())
                .param("token", token)
                .param("password", "abcd")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("Password updated", response);
    }

    @Test
    void resetUserPassword_NOT_FOUND() throws Exception {
        UserDto user = userListDto.get(clientIndex);
        String token = jwtUtil.generateToken(new UserSecurity(user));

        when(userRepoMock.findById(anyString())).thenReturn(Optional.empty());
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/reset")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", user.getId())
                .param("token", token)
                .param("password", "abcd")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals("User not found", response);
    }

    @Test
    void resetUserPassword_TOKEN_ERROR() throws Exception {
        UserDto user = userListDto.get(clientIndex);
        String token = "eyJhbGcgrewiOiJIUzI1NiJ9.eyJzdWIiOiJhbGJlcnQuZ3Jpc28ubWVuZGV6QGdtYWlsLmNvbSIsImV4cCI6MTU3OTE0MTUwOSwiaWF0IjoxNTc5MTA1NTA5fQ.MeOWSsMTx2ce6T6Loqa7agCKbqzB7VLIUli7zO8nQa0";

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/reset")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", user.getId())
                .param("token", token)
                .param("password", "abcd")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);

        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals("Invalid token", response);
    }

    @Test
    void resetUserPassword_TOKEN_EXPIRED() throws Exception {
        UserDto user = userListDto.get(clientIndex);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGJlcnQuZ3Jpc28ubWVuZGV6QGdtYWlsLmNvbSIsImV4cCI6MTU3OTE3MjgzOSwiaWF0IjoxNTc5MTcyODM5fQ.K3bhFEyfceFV5eSYC7XsjTf261N9TU7N9tc6AMpO53E";

        when(userRepoMock.findById(anyString())).thenReturn(Optional.of(userMapper.toUserDB(user)));
        when(userRepoMock.save(any(UserDB.class))).thenReturn(userMapper.toUserDB(user));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/reset")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("id", user.getId())
                .param("token", token)
                .param("password", "abcd")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);

        String response = mvcResult.getResponse().getErrorMessage();
        assertEquals("Time has expired", response);
    }
}