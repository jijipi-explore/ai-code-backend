package com.jjp.core.saver;

import com.jjp.ai.model.HtmlCodeResult;
import com.jjp.ai.model.MultiFileCodeResult;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 代码文件保存执行器
 * @Version: 1.0
 */

public class CodeFileSaverExecutor {
    private static final HtmlCodeFileSaverTemplate HTML_CODE_FILE_SAVER_TEMPLATE =
            new HtmlCodeFileSaverTemplate();
    private static final MultiFileCodeFileSaverTemplate MULTI_FILE_CODE_FILE_SAVER_TEMPLATE =
            new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     * @param codeResult 代码结果对象
     * @param codeGenTypeEnum 代码生成类型
     * @param appId 应用id
     * @return 保存后的文件对象
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        return switch (codeGenTypeEnum) {
            case HTML -> HTML_CODE_FILE_SAVER_TEMPLATE.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> MULTI_FILE_CODE_FILE_SAVER_TEMPLATE.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码类型" + codeGenTypeEnum);
        };
    }
}
