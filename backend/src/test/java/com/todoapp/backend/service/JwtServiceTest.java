package com.todoapp.backend.service;

import com.todoapp.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String fakeSecret = "6A748564B31FAF6A748564B31FAF6A748564B31FAF6A748564B31FAF6A748564";
        ReflectionTestUtils.setField(jwtService, "secretKey", fakeSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000 * 60 * 60);
    }

    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtService.generateToken("oguzhan");
        assertNotNull(token);
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken("oguzhan");
        String username = jwtService.extractUsername(token);
        assertEquals("oguzhan", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidUser() {
        String token = jwtService.generateToken("oguzhan");
        UserDetails user = new User("oguzhan", "123");

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_shouldReturnFalseForInvalidUser() {
        String token = jwtService.generateToken("oguzhan");
        UserDetails otherUser = new User("hacker", "123");

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }
}