package com.jjp.service;

import com.jjp.model.dto.chathistory.ChatHistoryQueryRequest;
import com.jjp.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.jjp.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author 积极皮
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加对话历史
     * @param appId 应用id
     * @param message 聊天消息
     * @param messageType 聊天消息类型
     * @param userId 用户id
     * @return 是否添加成功
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用id删除对话历史
     * @param appId 应用id
     * @return 是否删除成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 获取聊天记录查询包装类
     * @param chatHistoryQueryRequest 聊天记录查询请求
     * @return 查询包装类
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 分页获取应用聊天记录
     * @param appId 应用id
     * @param pageSize 每页大小
     * @param lastCreateTime 最后创建时间
     * @param loginUser 登录用户
     * @return 聊天记录分页
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime, User loginUser);

    /**
     * 加载数据库对话历史到记忆中
     * @param appId 应用id
     * @param chatMemory 记忆
     * @param maxCount 最大数量
     * @return 加载数量
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
