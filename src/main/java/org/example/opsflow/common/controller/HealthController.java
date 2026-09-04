package org.example.opsflow.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @SecurityRequirements
    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public String health(){
        return "ok";
    }
}
