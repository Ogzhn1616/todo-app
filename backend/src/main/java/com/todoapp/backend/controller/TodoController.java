package com.todoapp.backend.controller;

import com.todoapp.backend.model.Todo;
import com.todoapp.backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo, @AuthenticationPrincipal UserDetails userDetails) {
        return todoService.createTodo(todo, userDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        return todoService.getTodoById(id, userDetails);
    }

    @GetMapping
    public List<Todo> getTodosForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return todoService.getTodosForUser(userDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id,
                                           @RequestBody Todo updatedTodo,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        return todoService.updateTodo(id, updatedTodo, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        return todoService.deleteTodo(id, userDetails);
    }
}