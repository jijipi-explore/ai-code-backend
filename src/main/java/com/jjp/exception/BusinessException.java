package com.jjp.exception;

import lombok.Getter;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 自定义业务异常类，继承自RuntimeException,用于处理业务逻辑中的异常情况
 * @Version: 1.0
 */

@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码，用于标识具体的错误类型
     */
    private final Integer code;

    /**
     * 构造方法1：通过自定义的错误码和错误信息创建异常对象
     * @param code 错误码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法2：通过预定义的错误枚举创建异常对象
     * @param errorCode 错误码枚举，包含错误码和错误信息
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 构造方法3：通过预定义的错误枚举和自定义的错误信息创建异常对象
     * @param errorCode 错误码枚举，用于获取错误码
     * @param message 自定义的错误信息，会覆盖枚举中的默认错误信息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
