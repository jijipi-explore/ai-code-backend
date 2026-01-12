package com.jjp.common;

import com.jjp.exception.ErrorCode;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 响应结果返回工具类
 * @Version: 1.0
 */

public class ResultUtils {
    /**
     * 成功响应
     * 用于生成带有数据的成功响应结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data) {
        // 创建并返回一个成功的BaseResponse对象，状态码为0，数据为传入参数，消息为"ok"
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * 使用错误码生成错误响应
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        // 创建并返回一个错误的BaseResponse对象，使用传入的错误码
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * 使用自定义错误码和错误信息生成错误响应
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 响应
     */
    public static BaseResponse<?> error(int code, String message) {
        // 创建并返回一个错误的BaseResponse对象，使用传入的错误码和错误信息，数据为null
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     * 使用错误码和自定义错误信息生成错误响应
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        // 创建并返回一个错误的BaseResponse对象，使用错误码中的代码和传入的错误信息，数据为null
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
