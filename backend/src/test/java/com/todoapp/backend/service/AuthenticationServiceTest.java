package com.todoapp.backend.service;


import com.todoapp.backend.model.AuthRequest;
import com.todoapp.backend.model.AuthResponse;
import com.todoapp.backend.model.User;
import com.todoapp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldSaveUserAndReturnToken() {
        AuthRequest request = new AuthRequest("oguzhan", "123");
        User savedUser = new User("oguzhan", "encodedPw");

        when(passwordEncoder.encode("123")).thenReturn("encodedPw");
        when(userRepository.save(any())).thenReturn(savedUser);
        when(jwtService.generateToken("oguzhan")).thenReturn("fake-jwt");

        AuthResponse response = authenticationService.register(request);

        assertEquals("fake-jwt", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_shouldReturnTokenIfCredentialsValid() {
        AuthRequest request = new AuthRequest("oguzhan", "123");
        User user = new User("oguzhan", "encodedPw");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(userRepository.findByUsername("oguzhan"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken("oguzhan"))
                .thenReturn("valid-token");

        AuthResponse response = authenticationService.authenticate(request);

        assertEquals("valid-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}