package com.jjp.core;

import cn.hutool.json.JSONUtil;
import com.jjp.ai.AiCodeGeneratorService;
import com.jjp.ai.AiCodeGeneratorServiceFactory;
import com.jjp.ai.model.HtmlCodeResult;
import com.jjp.ai.model.MultiFileCodeResult;
import com.jjp.ai.model.message.AiResponseMessage;
import com.jjp.ai.model.message.ToolExecutedMessage;
import com.jjp.ai.model.message.ToolRequestMessage;
import com.jjp.core.parser.CodeParserExecutor;
import com.jjp.core.saver.CodeFileSaverExecutor;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI 代码生成外观类，组合生成和保存功能
 * @Version: 1.0
 */

@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService; // 注入AI代码生成服务，负责实际的代码生成逻辑

    @Resource
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory; // 注入AI代码生成服务工厂

//    /**
//     * 统一入口：根据类型生成并保存代码
//     * 该方法是整个代码生成流程的统一入口，根据不同的代码生成类型，调用相应的生成和保存方法
//     *
//     * @param userMessage     用户提示词，包含用户期望生成的代码描述
//     * @param codeGenTypeEnum 生成类型，枚举类型，指定代码生成的模式
//     * @return 保存的目录
//     */
//    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
//        if (codeGenTypeEnum == null) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
//        }
//        return switch (codeGenTypeEnum) {
//            case HTML -> generateAndSaveHtmlCode(userMessage);
//            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
//            default -> {
//                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
//            }
//        };
//    }
//
//    /**
//     * 统一入口：根据类型生成并保存代码（流式）
//     *
//     * @param userMessage     用户提示词
//     * @param codeGenTypeEnum 生成类型
//     */
//    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
//        if (codeGenTypeEnum == null) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
//        }
//        return switch (codeGenTypeEnum) {
//            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
//            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
//            default -> {
//                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
//            }
//        };
//    }

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId 应用id
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 根据 appId 获取相应的 AI 服务实例
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = service.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = service.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @param appId 应用id
     * @return 流式返回
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        // 根据 appId 获取相应的 AI 服务实例
        AiCodeGeneratorService service = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = service.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = service.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = service.generateVueProjectCodeStream(appId, userMessage);
                yield processTokenStream(tokenStream);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 生成 HTML 模式的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    /**
     * 生成多文件模式的代码并保存
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

    /**
     * 生成 HTML 模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                .doOnNext(chunk -> {
                    // 实时收集代码片段
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeHtmlCode = codeBuilder.toString();
                        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                        log.info("生成 HTML 模式的代码并保存（流式）成功，路径为：{}", savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("生成 HTML 模式的代码并保存（流式）失败: {}", e.getMessage());
                    }
                });
    }

    /**
     * 生成多文件模式的代码并保存（流式）
     *
     * @param userMessage 用户提示词
     * @return 保存的目录
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        return result
                .doOnNext(chunk -> {
                    // 实时收集代码片段
                    codeBuilder.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeMultiFileCode = codeBuilder.toString();
                        MultiFileCodeResult multiFileResult = CodeParser.parseMultiFileCode(completeMultiFileCode);
                        // 保存代码到文件
                        File savedDir = CodeFileSaver.saveMultiFileCodeResult(multiFileResult);
                        log.info("生成多文件模式的代码并保存（流式）成功，路径为：{}", savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("生成多文件模式的代码并保存（流式）失败: {}", e.getMessage());
                    }
                });
    }

    /**
     * 通用流式代码处理方法
     *
     * @param codeStream  代码流
     * @param codeGenType 代码生成类型
     * @param appId 应用id
     * @return 流式响应
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(() -> {
            // 流式返回完成后保存代码
            try {
                String completeCode = codeBuilder.toString();
                // 使用执行器解析代码
                Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                // 使用执行器保存代码
                File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                log.info("通用流式代码处理方法保存成功，路径为：{}", savedDir.getAbsolutePath());
            } catch (Exception e) {
                log.error("通用流式代码处理方法保存失败: {}", e.getMessage());
            }
        });
    }

    /**
     * 将TokenStream 转为 Flux<String>，并传递工具调用信息
     * @param tokenStream 输入的TokenStream流
     * @return 返回一个Flux<String>流，包含AI响应消息、工具请求消息和工具执行消息的JSON字符串
     */
    private Flux<String> processTokenStream(TokenStream tokenStream) {
        return Flux.create(
                sink -> { // 创建一个Flux sink用于发送事件
                    tokenStream
                            .onPartialResponse( // 处理部分响应事件
                                    (String partialResponse) -> { // 当接收到部分响应时
                                        AiResponseMessage aiResponseMessage = // 创建AI响应消息对象
                                                new AiResponseMessage(partialResponse);
                                        sink.next(JSONUtil.toJsonStr(aiResponseMessage)); // 将消息转为JSON并发送
                                    })
                            .onPartialToolExecutionRequest( // 处理部分工具执行请求事件
                                    (index, toolExecutionRequest) -> { // 当接收到工具执行请求时
                                        ToolRequestMessage toolRequestMessage = // 创建工具请求消息对象
                                                new ToolRequestMessage(toolExecutionRequest);
                                        sink.next(JSONUtil.toJsonStr(toolRequestMessage)); // 将消息转为JSON并发送
                                    }
                            )
                            .onToolExecuted( // 处理工具执行完成事件
                                    (ToolExecution toolExecution) -> { // 当工具执行完成时
                                        ToolExecutedMessage toolExecutedMessage = // 创建工具执行完成消息对象
                                                new ToolExecutedMessage(toolExecution);
                                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage)); // 将消息转为JSON并发送
                                    }
                            )
                            .onCompleteResponse( // 处理响应完成事件
                                    (ChatResponse chatResponse) -> { // 当响应完成时
                                        sink.complete(); // 完成流
                                    }
                            )
                            .onError( // 处理错误事件
                                    (Throwable error) -> { // 当发生错误时
                                        log.error(error.getMessage()); // 记录错误日志
                                        sink.error(error); // 将错误传递给流
                                    }
                            )
                            .start(); // 启动TokenStream处理
                }
        );
    }
}
