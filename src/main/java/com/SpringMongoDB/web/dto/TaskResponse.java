package com.SpringMongoDB.web.dto;

import com.SpringMongoDB.model.TaskStatus;
import com.SpringMongoDB.model.User;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class TaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds = new HashSet<>();

    private User author;

    private User assignee;

    private Set<User> observers = new HashSet<>();
}
