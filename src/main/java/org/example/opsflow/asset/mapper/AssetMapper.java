package org.example.opsflow.asset.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.asset.entity.Asset;

import java.util.List;


@Mapper
public interface AssetMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Asset record);

    int insertSelective(Asset record);

    Asset findById(Long id);

    int updateByPrimaryKeySelective(Asset record);

    int updateAsset(Asset record);

    List<Asset> findAll();

    int updateAssetStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateAssetAssignee(@Param("id") Long id,@Param("assignedUserId") Long assignedUserId);
}
