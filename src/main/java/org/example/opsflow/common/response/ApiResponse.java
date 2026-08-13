package org.example.opsflow.common.response;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data){
            return new ApiResponse<T>(200,"success",data);
    }
    public static <T> ApiResponse<Void> success(){
        return new ApiResponse<>(200,"success",null);
    }

}
