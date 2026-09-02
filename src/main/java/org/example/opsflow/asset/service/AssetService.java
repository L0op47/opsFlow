package org.example.opsflow.asset.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.opsflow.asset.dto.AssetResponse;
import org.example.opsflow.asset.dto.CreateAssetRequest;
import org.example.opsflow.asset.dto.UpdateAssetRequest;
import org.example.opsflow.common.response.PageResponse;

public interface AssetService {
    AssetResponse createAsset(@Valid CreateAssetRequest request);

    AssetResponse getAssetDetail(Long id);

    PageResponse<AssetResponse> getAssets(int page, int size);

    AssetResponse updateAsset(Long id, @Valid UpdateAssetRequest request);

    void updateAssetStatus(Long id, @NotNull(message = "资产状态不能为空") @Min(value = 0, message = "部门状态只能是0或者1") @Max(value = 1, message = "部门状态只能是0或者1") Integer status);

    void updateAssetAssignee(Long id, @NotNull @Positive Long assignedUserId);
}
