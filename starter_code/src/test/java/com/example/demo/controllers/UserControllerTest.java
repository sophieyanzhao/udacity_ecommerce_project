package com.example.demo.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.persistence.User;
import com.example.demo.TestUtils;



public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private CartRepository cartRepo = mock(CartRepository.class);


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
    }

    @Test
    public void create_user() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        final ResponseEntity<?> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertNotEquals("testPassword", u.getPassword());

        CreateUserRequest shortPassword = new CreateUserRequest();
        shortPassword.setPassword("short");
        shortPassword.setConfirmPassword("short");
        shortPassword.setUsername("test");
        final ResponseEntity<?> shortPasswordResponse = userController.createUser(shortPassword);
        assertNotNull(shortPasswordResponse);
        assertEquals(400, shortPasswordResponse.getStatusCodeValue());

    }

    @Test
    public void findUser(){
        String username = "test";
        ResponseEntity<User> responseByName = userController.findByUserName(username);
        assertNotNull(responseByName);
        assertEquals(404, responseByName.getStatusCodeValue());

        Long userId = 1L;
        ResponseEntity<User> responseById = userController.findById(userId);
        assertNotNull(responseById);
        assertEquals(404, responseById.getStatusCodeValue());
    }
}
