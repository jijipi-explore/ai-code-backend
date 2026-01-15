package com.jjp.core.parser;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI返回结果解析器策略接口
 * @Version: 1.0
 */

public interface CodeParser<T> {
    /**
     * 解析代码内容
     * @param codeContent 原始内容
     * @return 解析后的结果对象
     */
    T parse(String codeContent);
}
