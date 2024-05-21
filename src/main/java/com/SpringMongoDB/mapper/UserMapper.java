package com.SpringMongoDB.mapper;

import com.SpringMongoDB.model.User;
import com.SpringMongoDB.web.dto.UserRequest;
import com.SpringMongoDB.web.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userRequestToUser(UserRequest request);

    @Mapping(source = "userId", target = "id")
    User userRequestToUser(String userId, UserRequest request);

    UserResponse userToUserResponse(User user);
}
