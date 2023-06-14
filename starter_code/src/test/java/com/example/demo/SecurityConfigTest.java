package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String createTestUser(String username, String password) throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/create")
        .content(objectMapper.writeValueAsString(createUserRequest))
        .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse result = mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        String response = result.getContentAsString();
        return response;
    }

    private String testUserLogin(String username, String password) throws Exception{
        String rawJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
        .content(rawJson)
        .contentType(MediaType.APPLICATION_JSON);
        
        MockHttpServletResponse result = mockMvc.perform(requestBuilder)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();
        String response = result.getHeaders("Authorization").get(0);
        return response;

    }

    @Test
    public void testGetItems() throws Exception{
        String createUserInformation = createTestUser("testUser","testPassword");
        String token = testUserLogin("testUser","testPassword");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/item").header("Authorization",token)).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/item")).andExpect(status().isForbidden());
    }
    }
