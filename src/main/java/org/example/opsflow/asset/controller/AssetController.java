package org.example.opsflow.asset.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.asset.dto.*;
import org.example.opsflow.asset.service.AssetService;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assets")
@PreAuthorize("hasAuthority('asset:manage')")
public class AssetController {
    private final AssetService assetService;

    @PostMapping
    public ApiResponse<AssetResponse> createAsset(@Valid @RequestBody CreateAssetRequest request){
        return ApiResponse.success(assetService.createAsset(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<AssetResponse> getAssetDetail(@PathVariable Long id){
        return ApiResponse.success(assetService.getAssetDetail(id));
    }

    @GetMapping()
    public ApiResponse<PageResponse<AssetResponse>> getAssetList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        return ApiResponse.success(assetService.getAssets(page,size));
    }

    @PutMapping("/{id}")
    public ApiResponse<AssetResponse> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetRequest request){
        return ApiResponse.success(assetService.updateAsset(id,request));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateAssetStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssetStatusRequest request){
        assetService.updateAssetStatus(id, request.getStatus());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/assignee")
    public ApiResponse<Void> updateAssetAssignee(
            @PathVariable Long id,
            @Valid @RequestBody AssignAssetUserRequest request){
        assetService.updateAssetAssignee(id,request.getAssignedUserId());
        return ApiResponse.success();
    }


}
