package org.example.opsflow.rabc.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoleRequest {
    @NotNull(message = "角色代码不能为空")
    @Size(max = 32,message = "角色代码不能超过32个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$",
            message = "部门代码必须以字母开头，且只能包含字母、数字、下划线和横线")
    private String code;

    @NotNull(message = "角色名称不能为空")
    @Size(max = 64,message = "角色代码不能超过32个字符")
    private String name;

    @Size(max = 255,message = "角色描述不能超过255个字符")
    private String description;
}
