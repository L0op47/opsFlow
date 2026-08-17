package org.example.opsflow.user.service;


import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.user.dto.UserResponse;


public interface UserService {
    PageResponse<UserResponse> getUserPage(int page, int size);

    void updateUserStatus(Long id, Integer status);
}
