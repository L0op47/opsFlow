package org.example.opsflow.auth.dto;

public record CurrentUserResponse(
        Long id,
        String username,
        String realName,
        String email,
        String phone,
        Long departmentId,
        Integer status) {
}
