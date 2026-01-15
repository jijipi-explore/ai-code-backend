package com.jjp.core.parser;

import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 代码解析执行器
 * @Version: 1.0
 */

public class CodeParserExecutor {

    private static final HtmlCodeParser HTML_CODE_PARSER = new HtmlCodeParser();
    private static final MultiFileCodeParser MULTI_FILE_CODE_PARSER = new MultiFileCodeParser();

    /**
     * 执行代码解析器
     * @param codeContent 代码内容
     * @param codeGenTypeEnum 代码生成类型
     * @return 解析结果（HtmlCodeResult或MultiFileCodeResult）
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> HTML_CODE_PARSER.parse(codeContent);
            case MULTI_FILE -> MULTI_FILE_CODE_PARSER.parse(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码类型" + codeGenTypeEnum);
        };
    }
}
