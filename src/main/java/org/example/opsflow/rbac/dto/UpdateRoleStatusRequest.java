package org.example.opsflow.rbac.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleStatusRequest {
    @NotNull(message = "角色状态不能为空")
    @Min(value = 0, message = "角色状态只能是0或者1")
    @Max(value = 1, message = "角色状态只能是0或者1")
    private Integer status;
}
