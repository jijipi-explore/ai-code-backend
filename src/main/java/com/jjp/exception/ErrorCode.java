package com.jjp.exception;

import lombok.Getter;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 错误码枚举
 * @Version: 1.0
 */

@Getter
public enum ErrorCode {
    // 枚举实例，每个实例包含错误码和对应的错误信息
    SUCCESS(0, "ok"),                           // 成功状态码
    PARAMS_ERROR(40000, "请求参数错误"),           // 请求参数错误
    NOT_LOGIN_ERROR(40100, "未登录"),             // 未登录错误
    NO_AUTH_ERROR(40101, "无权限"),               // 无权限错误
    NOT_FOUND_ERROR(40400, "请求数据不存在"),     // 请求数据不存在错误
    FORBIDDEN_ERROR(40300, "禁止访问"),           // 禁止访问错误
    SYSTEM_ERROR(50000, "系统内部异常"),           // 系统内部异常
    OPERATION_ERROR(50001, "操作失败");           // 操作失败

    /**
     * 状态码
     * 用于表示不同的错误类型
     */
    private final Integer code;

    /**
     * 信息
     * 用于描述具体的错误原因
     */
    private final String message;

    /**
     * 枚举构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
