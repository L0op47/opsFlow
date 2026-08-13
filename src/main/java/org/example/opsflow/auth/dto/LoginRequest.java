package org.example.opsflow.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "用户名不可以为空")
    @Size(min=3,max=50,message="用户名长度必须在3-50之间")
    private String username;

    @NotBlank(message = "密码不可以为空")
    @Size(min=8,max=18,message="密码长度必须在8-18之间")
    private String password;
}
