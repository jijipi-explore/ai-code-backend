package com.jjp.common;

import com.jjp.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 通用基础响应类，用于封装API返回结果
 * @Version: 1.0
 */

@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 响应消息
     */
    private String message;

    /**
     * 全参数构造方法
     * @param code 响应状态码
     * @param data 响应数据
     * @param message 响应消息
     */
    public BaseResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 不带消息的构造方法
     * @param code 响应状态码
     * @param data 响应数据
     */
    public BaseResponse(Integer code, T data) {
        this(code, data, "");
    }

    /**
     * 使用错误码构造响应
     * @param errorCode 错误码枚举
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
