package com.example.testassignment.controller;

import com.example.testassignment.model.Users;
import com.example.testassignment.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UsersController.class)
public class UsersControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @Test
    public void testCreateUser_ValidEmail_ReturnsOk() throws Exception {
        Users user = new Users();
        user.setEmail("testEmail@example.com");
        user.setFirstName("Ben");
        user.setLastName("Karl");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"testEmail@example.com\",\"firstName\":\"Ben\",\"lastName\":\"Karl\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalidemail\",\"firstName\":\"Ben\",\"lastName\":\"Karl\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid email format"));
    }

    @Test
    public void testGetUser_InvalidDateRange_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .param("from", "2023-01-01")
                        .param("to", "2022-01-01"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("'From' date should be less than 'To' date"));
    }

    @Test
    public void testGetUser_NoUsersFound_ReturnsOkWithMessage() throws Exception {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(1);

        when(usersService.getUsersByBirthdateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("No users found in the specified date range"));
    }
}
