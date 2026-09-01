package org.example.opsflow.asset.converter;

import org.example.opsflow.asset.dto.AssetResponse;
import org.example.opsflow.asset.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AssetConverter {
    AssetResponse toAssetResponse(Asset asset);

    List<AssetResponse> toAssetResponseList(List<Asset> assets);
}
