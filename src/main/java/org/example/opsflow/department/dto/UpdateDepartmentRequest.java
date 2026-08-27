package org.example.opsflow.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDepartmentRequest {
    @NotBlank(message = "部门名字不能为空")
    @Size(max = 64,message = "部门名字不能超过64个字符")
    private String name;

    @NotBlank(message = "部门代码不能为空")
    @Size(max = 64,message = "部门代码不能超过32个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$",
            message = "部门代码必须以字母开头，且只能包含字母、数字、下划线和横线")
    private String code;
}
