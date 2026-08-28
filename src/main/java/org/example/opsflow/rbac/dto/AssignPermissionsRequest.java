package org.example.opsflow.rbac.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class AssignPermissionsRequest {
    @NotEmpty(message = "权限ID列表不能为空")
    private Set<
            @NotNull(message = "权限的ID不能为空")
            @Positive(message = "权限ID必须大于0")
                    Long> permissionIds;
}
