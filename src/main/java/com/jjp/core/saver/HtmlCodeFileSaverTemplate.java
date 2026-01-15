package com.jjp.core.saver;

import cn.hutool.core.util.StrUtil;
import com.jjp.ai.model.HtmlCodeResult;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: HTML代码文件保存器
 * @Version: 1.0
 */

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected CodeGenTypeEnum getCodeGenTypeEnum() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码为空");
        }
    }
}
