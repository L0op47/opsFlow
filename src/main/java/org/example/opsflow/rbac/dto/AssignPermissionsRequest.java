package org.example.opsflow.rbac.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class AssignPermissionsRequest {
    @NotEmpty(message = "角色ID列表不能为空")
    private Set<
            @NotNull(message = "角色的ID不能为空")
            @Positive(message = "角色ID必须大于0")
                    Long> permissionIds;
}
