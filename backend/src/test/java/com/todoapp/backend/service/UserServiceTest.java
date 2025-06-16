package com.todoapp.backend.service;

import com.todoapp.backend.model.User;
import com.todoapp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserExists() {
        User user = new User("oguzhan", "123");
        when(userRepository.findByUsername("oguzhan")).thenReturn(Optional.of(user));

        User returnedUser = (User) userService.loadUserByUsername("oguzhan");

        assertEquals("oguzhan", returnedUser.getUsername());
        assertEquals("123", returnedUser.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("hacker")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("hacker")
        );
    }
}