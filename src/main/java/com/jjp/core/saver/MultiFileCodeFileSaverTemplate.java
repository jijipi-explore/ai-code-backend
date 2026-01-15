package com.jjp.core.saver;

import cn.hutool.core.util.StrUtil;
import com.jjp.ai.model.MultiFileCodeResult;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 多文件代码保存器
 * @Version: 1.0
 */

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {
    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // 保存html文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // 保存css文件
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        // 保存js文件
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected CodeGenTypeEnum getCodeGenTypeEnum() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // 至少有html代码
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码为空");
        }
    }
}
