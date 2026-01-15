package com.jjp.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jjp.ai.model.HtmlCodeResult;
import com.jjp.ai.model.MultiFileCodeResult;
import com.jjp.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: CodeFileSaver 类用于保存不同类型的代码生成结果到文件系统
 * @Version: 1.0
 */

public class CodeFileSaver {

    // 文件保存根目录，使用系统当前工作目录下的tmp/code_output作为根目录
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存HTML代码结果到文件
     * @param result 包含HTML代码的结果对象
     * @return 保存HTML文件的目录File对象
     */
    public static File saveHtmlCodeResult(HtmlCodeResult result) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件代码结果（HTML、CSS、JS）到文件系统
     * @param result 包含HTML、CSS、JS代码的结果对象
     * @return 保存代码文件的目录File对象
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult result) {
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
        return new File(baseDirPath);
    }

    /**
     * 构建唯一的目录路径
     * @param bizType 业务类型标识
     * @return 生成的唯一目录路径
     */
    private static String buildUniqueDir(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 将内容写入指定文件
     * @param dirPath 目录路径
     * @param filename 文件名
     * @param content 要写入的内容
     */
    private static void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }
}
