package org.example.opsflow.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "系统接口", description = "系统健康检查")
public class HealthController {
    @SecurityRequirements
    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public String health(){
        return "ok";
    }
}
