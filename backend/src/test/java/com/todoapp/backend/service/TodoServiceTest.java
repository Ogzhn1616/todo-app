package com.todoapp.backend.service;

import com.todoapp.backend.model.Todo;
import com.todoapp.backend.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private UserDetails mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User("oguzhan", "password", new ArrayList<>());
    }

    @Test
    void createTodo_shouldAssignIdAndUsername() {
        Todo input = new Todo(null, "New Task", false, null);
        when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Todo saved = todoService.createTodo(input, mockUser);

        assertNotNull(saved.getId());
        assertEquals("oguzhan", saved.getUsername());
        verify(todoRepository).save(any());
    }

    @Test
    void getTodoById_shouldReturnTodoIfOwner() {
        Todo todo = new Todo("id1", "Test", false, "oguzhan");
        when(todoRepository.findById("id1")).thenReturn(Optional.of(todo));

        ResponseEntity<Todo> response = todoService.getTodoById("id1", mockUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todo, response.getBody());
    }

    @Test
    void getTodoById_shouldReturnForbiddenIfNotOwner() {
        Todo todo = new Todo("id1", "Test", false, "someone");
        when(todoRepository.findById("id1")).thenReturn(Optional.of(todo));

        ResponseEntity<Todo> response = todoService.getTodoById("id1", mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTodoById_shouldReturnForbiddenIfNotFound() {
        when(todoRepository.findById("id1")).thenReturn(Optional.empty());

        ResponseEntity<Todo> response = todoService.getTodoById("id1", mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTodosForUser_shouldReturnUserTodos() {
        Todo todo = new Todo("id1", "Test", false, "oguzhan");
        when(todoRepository.findByUsername("oguzhan")).thenReturn(List.of(todo));

        List<Todo> result = todoService.getTodosForUser(mockUser);
        assertEquals(1, result.size());
        assertEquals("oguzhan", result.get(0).getUsername());
    }

    @Test
    void updateTodo_shouldUpdateIfOwner() {
        String id = "id1";
        Todo existing = new Todo(id, "Old", false, "oguzhan");
        Todo update = new Todo(id, "Updated", true, null);

        when(todoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Todo> response = todoService.updateTodo(id, update, mockUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Todo result = response.getBody();
        assertNotNull(result);
        assertEquals("Updated", result.getTitle());
        assertEquals("oguzhan", result.getUsername());
    }

    @Test
    void updateTodo_shouldReturnForbiddenIfNotOwner() {
        Todo todo = new Todo("id1", "Task", false, "someone");
        when(todoRepository.findById("id1")).thenReturn(Optional.of(todo));

        ResponseEntity<Todo> response = todoService.updateTodo("id1", new Todo(), mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateTodo_shouldReturnForbiddenIfNotFound() {
        when(todoRepository.findById("id1")).thenReturn(Optional.empty());

        ResponseEntity<Todo> response = todoService.updateTodo("id1", new Todo(), mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteTodo_shouldDeleteIfOwner() {
        Todo todo = new Todo("id1", "Task", false, "oguzhan");
        when(todoRepository.findById("id1")).thenReturn(Optional.of(todo));

        ResponseEntity<Void> response = todoService.deleteTodo("id1", mockUser);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(todoRepository).deleteById("id1");
    }

    @Test
    void deleteTodo_shouldReturnForbiddenIfNotOwner() {
        Todo todo = new Todo("id1", "Task", false, "someone");
        when(todoRepository.findById("id1")).thenReturn(Optional.of(todo));

        ResponseEntity<Void> response = todoService.deleteTodo("id1", mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(todoRepository, never()).deleteById(any());
    }

    @Test
    void deleteTodo_shouldReturnForbiddenIfNotFound() {
        when(todoRepository.findById("id1")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = todoService.deleteTodo("id1", mockUser);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }
}