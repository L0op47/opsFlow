package org.example.opsflow.asset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAssetRequest {
    @NotBlank
    @Size(max = 32,message = "资产编号不能超过32个字符")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$",
            message = "资产编号必须以字母开头，且只能包含字母、数字、下划线和横线")
    private String assetNo;
    @NotBlank
    @Size(max = 100,message = "资产名称不能超过100个字符")
    private String name;
    @NotBlank
    @Size(max = 32,message = "资产类型不能超过32个字符")
    private String category;
    @Size(max=100,message = "资产型号不能超过100个字符")
    private String model;
}
