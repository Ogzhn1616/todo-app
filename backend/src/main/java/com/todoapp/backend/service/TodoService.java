package com.todoapp.backend.service;

import com.todoapp.backend.model.Todo;
import com.todoapp.backend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo createTodo(Todo todo, UserDetails userDetails) {
        todo.setId(UUID.randomUUID().toString());
        todo.setUsername(userDetails.getUsername());
        return todoRepository.save(todo);
    }

    public ResponseEntity<Todo> getTodoById(String id, UserDetails userDetails) {
        Optional<Todo> todoOpt = checkTodoOwnership(id, userDetails);

        if (todoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(todoOpt.get());
    }

    public List<Todo> getTodosForUser(UserDetails userDetails) {
        return todoRepository.findByUsername(userDetails.getUsername());
    }

    public ResponseEntity<Todo> updateTodo(String id, Todo updatedTodo, UserDetails userDetails) {
        Optional<Todo> ownedTodo = checkTodoOwnership(id, userDetails);
        if (ownedTodo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        updatedTodo.setId(id);
        updatedTodo.setUsername(userDetails.getUsername());
        return ResponseEntity.ok(todoRepository.save(updatedTodo));
    }

    public ResponseEntity<Void> deleteTodo(String id, UserDetails userDetails) {
        Optional<Todo> ownedTodo = checkTodoOwnership(id, userDetails);
        if (ownedTodo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        todoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Optional<Todo> checkTodoOwnership(String id, UserDetails userDetails) {
        Optional<Todo> todoOpt = todoRepository.findById(id);
        if (todoOpt.isEmpty()) return Optional.empty();

        Todo todo = todoOpt.get();
        if (!todo.getUsername().equals(userDetails.getUsername())) {
            return Optional.empty();
        }

        return Optional.of(todo);
    }
}