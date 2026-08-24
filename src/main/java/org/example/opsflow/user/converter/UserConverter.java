package org.example.opsflow.user.converter;

import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserConverter {
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
}
