package org.example.opsflow.common.response;

import org.example.opsflow.common.exception.ErrorCode;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data){
            return new ApiResponse<>(0,"success",data);
    }
    public static ApiResponse<Void> success(){
        return new ApiResponse<>(0,"success",null);
    }


    public static ApiResponse<Void> error(
            ErrorCode errorCode
    ) {
        return new ApiResponse<>(
                errorCode.getCode(),
                errorCode.getDefaultMessage(),
                null
        );
    }

    public static ApiResponse<Void> error(
            ErrorCode errorCode,
            String message
    ) {
        return new ApiResponse<>(
                errorCode.getCode(),
                message,
                null
        );
    }
}
