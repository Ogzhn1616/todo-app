package com.todoapp.backend.repository;

import com.todoapp.backend.model.Todo;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends CouchbaseRepository<Todo, String> {
    List<Todo> findByUsername(String username);
}
