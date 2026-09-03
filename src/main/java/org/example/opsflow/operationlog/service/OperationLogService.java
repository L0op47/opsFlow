package org.example.opsflow.operationlog.service;

import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.dto.OperationLogResponse;
import org.example.opsflow.operationlog.entity.OperationLogRecord;

public interface OperationLogService {
    void save(OperationLogRecord record);

    PageResponse<OperationLogResponse> findAll(int page, int size, String operatorName, String module, Integer success);
}
