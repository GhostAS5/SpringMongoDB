package com.SpringMongoDB.mapper;

import com.SpringMongoDB.model.User;
import com.SpringMongoDB.web.dto.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userRequestToUser(UserRequest request);
}
