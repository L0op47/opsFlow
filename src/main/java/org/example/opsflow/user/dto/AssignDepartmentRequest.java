package org.example.opsflow.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AssignDepartmentRequest {
    @NotNull(message = "部门ID不可以为空")
    @Positive(message = "部门ID必须大于1")
    private Long departmentId;
}
