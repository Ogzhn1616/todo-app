package com.todoapp.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String id;
    private String title;
    private boolean completed;
    private String username;

}
