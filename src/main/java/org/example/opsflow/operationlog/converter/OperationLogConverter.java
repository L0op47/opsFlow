package org.example.opsflow.operationlog.converter;

import org.example.opsflow.operationlog.dto.OperationLogResponse;
import org.example.opsflow.operationlog.entity.OperationLogRecord;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OperationLogConverter {
    List<OperationLogResponse> toOperationLogResponseList(List<OperationLogRecord> operationLogRecords);
}
