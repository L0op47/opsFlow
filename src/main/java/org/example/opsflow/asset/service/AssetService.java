package org.example.opsflow.asset.service;

import jakarta.validation.Valid;
import org.example.opsflow.asset.dto.AssetResponse;
import org.example.opsflow.asset.dto.CreateAssetRequest;
import org.example.opsflow.common.response.PageResponse;

public interface AssetService {
    AssetResponse createAsset(@Valid CreateAssetRequest request);

    AssetResponse getAssetDetail(Long id);

    PageResponse<AssetResponse> getAssets(int page, int size);
}
