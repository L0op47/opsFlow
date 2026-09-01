package org.example.opsflow.asset.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.asset.converter.AssetConverter;
import org.example.opsflow.asset.dto.AssetResponse;
import org.example.opsflow.asset.dto.CreateAssetRequest;
import org.example.opsflow.asset.entity.Asset;
import org.example.opsflow.asset.mapper.AssetMapper;
import org.example.opsflow.asset.service.AssetService;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetMapper assetMapper;
    private final AssetConverter assetConverter;

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


}
