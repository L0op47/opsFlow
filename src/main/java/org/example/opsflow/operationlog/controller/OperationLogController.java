package org.example.opsflow.operationlog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.dto.OperationLogResponse;
import org.example.opsflow.operationlog.service.OperationLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operation-logs")
@PreAuthorize("hasAuthority('role:manage')")
@Tag(
        name = "操作日志",
        description = "查询系统关键写操作的审计记录"
)
public class OperationLogController {
    private final OperationLogService operationLogService;

    @GetMapping
    @Operation(
            summary = "分页查询操作日志",
            description = "支持按操作人、业务模块和执行结果筛选"
    )
    public ApiResponse<PageResponse<OperationLogResponse>> getOperationLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String operatorUsername,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) Integer success
    ){
        return ApiResponse.success(operationLogService.findAll(
                page,
                size,
                operatorUsername,
                module,
                success
        ));
    }
}
