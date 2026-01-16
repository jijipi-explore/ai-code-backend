package com.jjp.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jjp.constant.AppConstant;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 文件保存器-模板方法模式
 * @Version: 1.0
 */

public abstract class CodeFileSaverTemplate<T> {

    // 文件保存根目录，使用系统当前工作目录下的tmp/code_output作为根目录
    protected static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     * 模板方法，保存代码结果
     * @param result 代码结果
     * @param appId 应用ID
     * @return 保存后的文件对象
     */
    public final File saveCode(T result, Long appId) {
        // 1.验证输入
        validateInput(result);
        // 2.构建目录
        String baseDirPath = buildBaseDirPath(appId);
        // 3.保存文件（由子类实现）
        saveFiles(result, baseDirPath);
        // 4.返回文件对象
        return new File(baseDirPath);
    }

    /**
     * 验证输入参数
     * @param result AI返回结果
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI返回结果为空");
        }
    }

    /**
     * 构建基础目录路径
     * @param appId 应用ID
     * @return 基础目录路径
     */
    protected String buildBaseDirPath(Long appId) {
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用id不能为空");
        }
        String codeType = getCodeGenTypeEnum().getValue();
        String dirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + dirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存文件的具体实现
     * @param result 代码结果对象
     * @param baseDirPath 基础目录路径
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * 获取代码生成类型
     * @return 代码生成类型
     */
    protected abstract CodeGenTypeEnum getCodeGenTypeEnum();

    /**
     * 将内容写入指定文件
     * @param dirPath 目录路径
     * @param filename 文件名
     * @param content 要写入的内容
     */
    protected final void writeToFile(String dirPath, String filename, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }
}
