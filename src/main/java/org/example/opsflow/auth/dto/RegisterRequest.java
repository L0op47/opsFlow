package org.example.opsflow.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不可以为空")
    @Size(min=3,max=50,message = "用户名长度必须在3-50之间")
    private String username;

    @NotBlank(message = "密码不可以为空")
    @Size(min=8,max=18,message = "密码长度必须在8-18之间")
    private String password;

    @Size(max=50,message = "真实姓名必须在50个字符以内")
    private String realName;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50,message = "邮箱长度必须在50字符以内")
    private String email;

    @Size(max=11,message = "电话号码必须在11字符以内")
    private String phone;
}
