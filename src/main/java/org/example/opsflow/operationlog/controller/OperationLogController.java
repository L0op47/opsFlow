package org.example.opsflow.operationlog.controller;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.dto.OperationLogResponse;
import org.example.opsflow.operationlog.service.OperationLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operation-logs")
public class OperationLogController {
    private final OperationLogService operationLogService;

    @GetMapping
    public ApiResponse<PageResponse<OperationLogResponse>> getOperationLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) Integer success
    ){
        return ApiResponse.success(operationLogService.findAll(
                page,
                size,
                operatorName,
                module,
                success
        ));
    }
}
