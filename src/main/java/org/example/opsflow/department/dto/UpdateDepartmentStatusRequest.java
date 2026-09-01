package org.example.opsflow.department.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDepartmentStatusRequest {
    @NotNull(message = "部门状态不能为空")
    @Min(value = 0, message = "部门状态只能是0或者1")
    @Max(value = 1, message = "部门状态只能是0或者1")
    private Integer status;
}
