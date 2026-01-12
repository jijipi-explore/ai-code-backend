package com.jjp.exception;

import com.jjp.common.BaseResponse;
import com.jjp.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 全局异常处理器
 * @Version: 1.0
 */

@Hidden // 使用@Hidden注解可能用于在API文档中隐藏此控制器
@RestControllerAdvice // 使用@RestControllerAdvice注解标记为全局异常处理类
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param e 业务异常对象
     * @return 返回包含错误信息的BaseResponse对象
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        // 记录错误日志
        log.error("BusinessException", e);
        // 返回业务异常的错误代码和消息
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理运行时异常
     * @param e 运行时异常对象
     * @return 返回包含系统错误信息的BaseResponse对象
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        // 记录错误日志
        log.error("RuntimeException", e);
        // 返回系统错误代码和固定错误消息
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
