package org.example.opsflow.operationlog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.operationlog.entity.OperationLogRecord;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    int insertSelective(OperationLogRecord record);

    List<OperationLogRecord> findAll(@Param("operatorUsername") String operatorName,@Param("module") String module,@Param("success") Integer success);
}
