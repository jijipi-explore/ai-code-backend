package com.jjp.exception;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 异常处理工具类，提供条件判断抛出异常的便捷方法
 * @Version: 1.0
 */

public class ThrowExceptionUtils {
    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     * 重载方法，使用错误码创建业务异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     * 重载方法，使用错误码和自定义消息创建业务异常
     *
     * @param condition 条件
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
