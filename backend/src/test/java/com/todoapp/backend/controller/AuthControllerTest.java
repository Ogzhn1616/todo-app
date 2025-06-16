package com.todoapp.backend.controller;

import com.todoapp.backend.model.AuthRequest;
import com.todoapp.backend.model.AuthResponse;
import com.todoapp.backend.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthenticationService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthenticationService.class);
        authController = new AuthController(authService);
    }

    @Test
    void register_shouldReturnToken() {
        AuthRequest request = new AuthRequest("oguzhan", "123");
        AuthResponse mockResponse = new AuthResponse("fake-jwt");

        when(authService.register(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fake-jwt", response.getBody().getToken());
    }

    @Test
    void login_shouldReturnToken() {
        AuthRequest request = new AuthRequest("oguzhan", "123");
        AuthResponse mockResponse = new AuthResponse("fake-jwt");

        when(authService.authenticate(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("fake-jwt", response.getBody().getToken());
    }


    @Test
    void register_shouldReturnDifferentTokenForDifferentUser() {
        AuthRequest request = new AuthRequest("mehmet", "123");
        AuthResponse mockResponse = new AuthResponse("jwt-for-mehmet");

        when(authService.register(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals("jwt-for-mehmet", response.getBody().getToken());
    }
}