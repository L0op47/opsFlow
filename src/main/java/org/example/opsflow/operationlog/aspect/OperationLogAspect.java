package org.example.opsflow.operationlog.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.opsflow.operationlog.annotation.OperationLog;
import org.example.opsflow.operationlog.entity.OperationLogRecord;
import org.example.opsflow.operationlog.service.OperationLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {
    private final HttpServletRequest request;
    private final OperationLogService operationLogService;

    @Around("@annotation(operationLog)")
    public Object recordOperation(
            ProceedingJoinPoint joinPoint,
            OperationLog operationLog
    )throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLogRecord record = new OperationLogRecord();
        record.setOperatorUsername(getOperatorName());
        record.setModule(operationLog.module());
        record.setAction(operationLog.action());
        record.setTargetId(
                getTargetId(joinPoint, operationLog.targetIdArg())
        );
        record.setRequestMethod(request.getMethod());
        record.setRequestUri(request.getRequestURI());
        record.setIpAddress(request.getRemoteAddr());
        try {
            Object result = joinPoint.proceed();
            record.setSuccess(1);
            return result;
        }catch (Throwable e){
            record.setSuccess(0);
            record.setErrorMessage(truncateErrorMessage(e.getMessage()));
            throw e;
        }finally {
            record.setDurationMs(System.currentTimeMillis() - startTime);
            saveOperationLogSafely(record);
        }
    }

    private String getOperatorName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null){
            return "anonymous";
        }
        return auth.getName();
    }

    private Long getTargetId(
            ProceedingJoinPoint joinPoint,
            int targetIdArg
    ){
        if(targetIdArg<0){
            return null;
        }
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length <= targetIdArg){
            return null;
        }
        Object targetId = args[targetIdArg];
        if(targetId instanceof Number number){
            return number.longValue();
        }
        return null;
    }

    private void saveOperationLogSafely(OperationLogRecord record) {
        try {
            operationLogService.save(record);
        } catch (Exception e) {
            log.error(
                    "保存操作日志失败，module={}, action={}",
                    record.getModule(),
                    record.getAction(),
                    e
            );
        }
    }

    private String truncateErrorMessage(String message) {
        if (message == null || message.length() <= 500) {
            return message;
        }

        return message.substring(0, 500);
    }


}
