package com.SpringMongoDB.mapper;

import com.SpringMongoDB.model.Task;
import com.SpringMongoDB.web.dto.TaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task taskRequestToTask(TaskRequest request);

}
