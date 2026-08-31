package org.example.opsflow.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 通用错误：10000～19999
    INVALID_REQUEST_PARAMETER(
            10001,
            HttpStatus.BAD_REQUEST,
            "请求参数不合法"
    ),

    INVALID_PAGE_PARAMETER(
            10002,
            HttpStatus.BAD_REQUEST,
            "分页参数不合法"
    ),

    // 认证与权限：20000～29999
    UNAUTHORIZED(
            20001,
            HttpStatus.UNAUTHORIZED,
            "未登录或登录状态无效"
    ),

    INVALID_TOKEN(
            20002,
            HttpStatus.UNAUTHORIZED,
            "登录凭证无效"
    ),

    FORBIDDEN(
            20003,
            HttpStatus.FORBIDDEN,
            "无权执行该操作"
    ),

    INVALID_CREDENTIALS(
            20004,
            HttpStatus.UNAUTHORIZED,
            "用户名或密码错误"
    ),

    ACCOUNT_DISABLED(
            20005,
            HttpStatus.FORBIDDEN,
            "用户已被禁用"
    ),

    // 用户模块：30000～39999
    USER_NOT_FOUND(
            30001,
            HttpStatus.NOT_FOUND,
            "用户不存在"
    ),

    USERNAME_ALREADY_EXISTS(
            30002,
            HttpStatus.CONFLICT,
            "用户名已存在"
    ),

    INVALID_USER_STATUS(
            30003,
            HttpStatus.BAD_REQUEST,
            "用户状态不合法"
    ),

    // 部门模块：40000～49999
    DEPARTMENT_NOT_FOUND(
            40001,
            HttpStatus.NOT_FOUND,
            "部门不存在"
    ),

    DEPARTMENT_CODE_ALREADY_EXISTS(
            40002,
            HttpStatus.CONFLICT,
            "部门代码已存在"
    ),

    DEPARTMENT_DISABLED(
            40003,
            HttpStatus.CONFLICT,
            "部门已被禁用"
    ),

    // 工单模块：50000～59999
    TICKET_NOT_FOUND(
            50001,
            HttpStatus.NOT_FOUND,
            "工单不存在"
    ),

    TICKET_ACCESS_DENIED(
            50002,
            HttpStatus.FORBIDDEN,
            "无权查看该工单"
    ),

    INVALID_TICKET_STATUS_TRANSITION(
            50003,
            HttpStatus.CONFLICT,
            "工单状态流转不合法"
    ),
    // RBAC模块：60000～69999
    ROLE_NOT_FOUND(
            60001,
            HttpStatus.NOT_FOUND,
            "角色不存在"
    ),

    ROLE_CODE_ALREADY_EXISTS(
            60002,
            HttpStatus.CONFLICT,
            "角色编码已存在"
    ),

    ROLE_DISABLED(
            60003,
            HttpStatus.CONFLICT,
            "角色已被禁用"
    ),

    PERMISSION_NOT_FOUND(
            60004,
            HttpStatus.NOT_FOUND,
            "权限不存在"
    ),

    PERMISSION_CODE_ALREADY_EXISTS(
            60005,
            HttpStatus.CONFLICT,
            "角色编码已存在"
    ),

    PERMISSION_DISABLED(
            60006,
            HttpStatus.CONFLICT,
            "权限已被禁用"
    ),

    // 系统错误：90000～99999
    INTERNAL_SERVER_ERROR(
            90001,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "系统内部错误"
    ),

    DATABASE_OPERATION_FAILED(
            90002,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "数据操作失败"
    );

    private final int code;

    private final HttpStatus httpStatus;

    private final String defaultMessage;
}