package com.SpringMongoDB.web.dto;

import com.SpringMongoDB.model.TaskStatus;
import lombok.Data;

@Data
public class TaskRequest {

    private String name;

    private String description;

    private String authorId;

    private String assigneeId;

    private TaskStatus status;
}
