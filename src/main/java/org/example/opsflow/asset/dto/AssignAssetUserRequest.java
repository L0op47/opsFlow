package org.example.opsflow.asset.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AssignAssetUserRequest {
    @NotNull
    @Positive
    private Long assignedUserId;
}
