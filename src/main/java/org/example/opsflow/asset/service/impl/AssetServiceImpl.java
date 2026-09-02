package org.example.opsflow.asset.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.asset.converter.AssetConverter;
import org.example.opsflow.asset.dto.AssetResponse;
import org.example.opsflow.asset.dto.CreateAssetRequest;
import org.example.opsflow.asset.dto.UpdateAssetRequest;
import org.example.opsflow.asset.entity.Asset;
import org.example.opsflow.asset.mapper.AssetMapper;
import org.example.opsflow.asset.service.AssetService;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetMapper assetMapper;
    private final AssetConverter assetConverter;
    private final UserMapper userMapper;

    @Override
    public AssetResponse createAsset(CreateAssetRequest request) {
        String name =  request.getName().trim();
        String assetNo =  request.getAssetNo().trim().toUpperCase(Locale.ROOT);
        Asset asset = new Asset();
        asset.setAssetNo(assetNo);
        asset.setName(name);
        asset.setCategory(request.getCategory());
        asset.setModel(request.getModel());
        asset.setStatus(1);
        int affectedRows;
        try{
            affectedRows = assetMapper.insert(asset);
            if(affectedRows != 1){
                throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"资产创建失败");
            }
        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.ASSET_CODE_ALREADY_EXISTS);
        }
        Asset savedAsset = assetMapper.findById(asset.getId());
        return assetConverter.toAssetResponse(savedAsset);
    }

    @Override
    public AssetResponse getAssetDetail(Long id) {
        Asset asset = assetMapper.findById(id);
        if(asset == null){
            throw new BusinessException(ErrorCode.ASSET_NOT_FOUND);
        }
        return assetConverter.toAssetResponse(asset);
    }

    @Override
    public PageResponse<AssetResponse> getAssets(int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        PageHelper.startPage(page,size);
        List<Asset> assets = assetMapper.findAll();
        PageInfo<Asset> pageInfo = new PageInfo<>(assets);
        List<AssetResponse> records = assetConverter.toAssetResponseList(assets);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }

    @Override
    public AssetResponse updateAsset(Long id, @Valid UpdateAssetRequest request) {
        Asset asset = exitAsset(id);
        String name =  request.getName().trim();
        String assetNo =  request.getAssetNo().trim().toUpperCase(Locale.ROOT);
        if(     Objects.equals(asset.getName(),name) &&
                Objects.equals(asset.getAssetNo(),assetNo) &&
                Objects.equals(asset.getModel(),request.getModel()) &&
                Objects.equals(asset.getCategory(),request.getCategory())
        ){
            return assetConverter.toAssetResponse(asset);
        }
        asset.setName(name);
        asset.setAssetNo(assetNo);
        asset.setCategory(request.getCategory());
        asset.setModel(request.getModel());
        try{
            int affectedRows = assetMapper.updateAsset(asset);
            if(affectedRows != 1){
                throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"资产修改失败");
            }
        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.ASSET_CODE_ALREADY_EXISTS);
        }
        Asset savedAsset = assetMapper.findById(asset.getId());
        return assetConverter.toAssetResponse(savedAsset);
    }

    @Override
    public void updateAssetStatus(Long id, Integer status) {
        Asset asset = exitAsset(id);
        if(Objects.equals(asset.getStatus(), status)){
            return;
        }
        int affectedRows = assetMapper.updateAssetStatus(id, status);
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"资产状态更新失败");
        }
    }

    @Override
    public void updateAssetAssignee(Long id, Long assignedUserId) {
        Asset asset = exitAsset(id);
        if(!Objects.equals(asset.getStatus(), 1)){
            throw new BusinessException(ErrorCode.ASSET_DISABLED);
        }
        User user = userMapper.findById(assignedUserId);
        if(user == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(!Objects.equals(user.getStatus(),1)){
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        if(Objects.equals(asset.getAssignedUserId(), assignedUserId)){
            return;
        }
        int affectedRows = assetMapper.updateAssetAssignee(id, assignedUserId);
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"资产分配失败");
        }
    }

    private Asset exitAsset(Long id){
        Asset asset = assetMapper.findById(id);
        if(asset == null){
            throw new BusinessException(ErrorCode.ASSET_NOT_FOUND);
        }
        return asset;
    }
}
