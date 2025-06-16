package com.todoapp.backend.controller;

import com.todoapp.backend.model.Todo;
import com.todoapp.backend.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    private UserDetails currentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = User.withUsername("ali")
                .password("password")
                .roles("USER")
                .build();
    }


    @Test
    void createTodo_ShouldReturnCreatedTodo() {
        Todo todo = new Todo("1", "Read book", false, "ali");
        when(todoService.createTodo(todo, currentUser)).thenReturn(todo);

        Todo result = todoController.createTodo(todo, currentUser);

        assertEquals(todo, result);
        verify(todoService).createTodo(todo, currentUser);
    }

    @Test
    void getTodoById_ShouldReturnTodo() {
        Todo todo = new Todo("2", "Do workout", false, "ali");
        when(todoService.getTodoById("2", currentUser)).thenReturn(ResponseEntity.ok(todo));

        ResponseEntity<?> response = todoController.getTodoById("2", currentUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todo, response.getBody());
    }

    @Test
    void getTodosForUser_ShouldReturnTodoList() {
        List<Todo> todos = List.of(
                    new Todo("1", "Go", false, "ali"),
                new Todo("2", "Finish", true, "ali")
        );
        when(todoService.getTodosForUser(currentUser)).thenReturn(todos);

        List<Todo> result = todoController.getTodosForUser(currentUser);

        assertEquals(2, result.size());
        verify(todoService).getTodosForUser(currentUser);
    }

    @Test
    void updateTodo_ShouldReturnUpdatedTodo() {
        Todo updatedTodo = new Todo("1", "Read-- updated", true, "ali");
        when(todoService.updateTodo("1", updatedTodo, currentUser)).thenReturn(ResponseEntity.ok(updatedTodo));

        ResponseEntity<?> response = todoController.updateTodo("1", updatedTodo, currentUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTodo, response.getBody());
    }

    @Test
    void deleteTodo_ShouldReturnNoContent() {
        when(todoService.deleteTodo("1", currentUser)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<?> response = todoController.deleteTodo("1", currentUser);

        assertEquals(HttpStatus.NO_CONTENT
                , response.getStatusCode());
        verify(todoService).deleteTodo("1", currentUser);
    }
}