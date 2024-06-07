package com.SpringMongoDB.mapper;

import com.SpringMongoDB.model.Task;
import com.SpringMongoDB.web.dto.TaskRequest;
import com.SpringMongoDB.web.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task taskRequestToTask(TaskRequest request);

    @Mapping(source = "taskId", target = "id")
    Task taskRequestToTask(String taskId, TaskRequest request);

    TaskResponse taskToTaskResponse(Task task);

}
