package com.rewards.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.model.Transfer;
import com.rewards.model.User;
import com.rewards.repository.UserRepository;
import com.rewards.service.UserService;
import com.rewards.web.request.CreateTransferRequest;
import com.rewards.web.request.CreateUserRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    @Test
    public void createUserSucceeds() throws Exception {
        //TODO more robust test that inspects the response
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateUserRequest("abc@example.org", "abby", "normal"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createUserFailsBecauseUserAlreadyExists() throws Exception {
        given(userRepository.findByEmail("abc@example.org"))
                .willReturn(new User("abc@example.org", "abby", "normal"));
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateUserRequest("abc@example.org", "abby", "normal"))))
                .andExpect(status().isConflict())
                .andExpect(status().reason("User already exists"));
    }

    @Test
    public void getTransfersSucceeds() throws Exception {
        //TODO more robust test utilities for inspecting transfer contents / setting transfers on a user
        User user = new User("abc@example.org", "abby", "normal");
        Set<Transfer> transfers = new HashSet<>();
        transfers.add(new Transfer(user, 10));
        transfers.add(new Transfer(user, 12));

        given(userRepository.findOne(anyLong()))
                .willReturn(user);

        mvc.perform(get("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void getTransfersFailsBecauseUserNotFound() throws Exception {
        mvc.perform(get("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(status().reason("User not found"));

    }

    @Test
    public void createTransferForUser() throws Exception {
        //TODO more robust test that inspects the response
        given(userRepository.findOne(anyLong()))
                .willReturn(new User("abc@example.org", "abby", "normal"));
        given(userService.calculateAdjustedPoints(any(User.class), anyLong()))
                .willReturn(1L);
        mvc.perform(post("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateTransferRequest(1, 15))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createTransferForUserFailsBecauseZero() throws Exception {
        mvc.perform(post("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateTransferRequest(1, 15))))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    @Test
    public void createTransferForUserFailsBecauseUserNotFound() throws Exception {
        mvc.perform(post("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateTransferRequest(1, 0))))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Invalid points adjustment amount request"));
    }

    @Test
    public void createTransferForUserFailsBecauseNegativeBalance() throws Exception {
        given(userRepository.findOne(anyLong()))
                .willReturn(new User("abc@example.org", "abby", "normal"));
        given(userService.calculateAdjustedPoints(any(User.class), anyLong()))
                .willReturn(-1L);
        mvc.perform(post("/api/users/1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new CreateTransferRequest(1, 15))))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Invalid points adjustment amount request"));
    }
}
