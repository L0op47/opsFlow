package org.example.opsflow.operationlog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.converter.OperationLogConverter;
import org.example.opsflow.operationlog.dto.OperationLogResponse;
import org.example.opsflow.operationlog.entity.OperationLogRecord;
import org.example.opsflow.operationlog.mapper.OperationLogMapper;
import org.example.opsflow.operationlog.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {
    private final OperationLogMapper operationLogMapper;
    private final OperationLogConverter operationLogConverter;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(OperationLogRecord record) {
        int affectedRows = operationLogMapper.insertSelective(record);
        if (affectedRows != 1) {
            throw new IllegalStateException("操作日志失败");
        }
    }

    @Override
    public PageResponse<OperationLogResponse> findAll(int page, int size, String operatorName, String module, Integer success) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        if(operatorName == null || operatorName.isEmpty()){
            operatorName = null;
        }
        if(module == null || module.isEmpty()){
            module = null;
        }
        PageHelper.startPage(page,size);

        List<OperationLogRecord> logs = operationLogMapper.findAll(operatorName,module,success);
        PageInfo<OperationLogRecord> pageInfo = new PageInfo<>(logs);
        List<OperationLogResponse> records = operationLogConverter.toOperationLogResponseList(logs);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }
}
