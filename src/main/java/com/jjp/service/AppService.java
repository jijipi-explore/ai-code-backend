package com.jjp.service;

import com.jjp.model.dto.app.AppQueryRequest;
import com.jjp.model.entity.User;
import com.jjp.model.vo.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.jjp.model.entity.App;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author 积极皮
 */
public interface AppService extends IService<App> {

    /**
     * 流式生成代码
     * @param appId 应用id
     * @param message prompt
     * @param loginUser 登录用户
     * @return 生成结果流
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     * @param appId 应用id
     * @param loginUser 登录用户
     * @return 部署url
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 获取应用返回信息
     * @param app 应用信息
     * @return 应用返回信息
     */
    AppVO getAppVO(App app);

    /**
     * 构造应用分页查询对象
     * @param appQueryRequest 应用查询请求
     * @return 应用分页查询对象
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 获取应用返回信息列表
     * @param appList 应用信息列表
     * @return 应用返回信息列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 根据应用id删除应用
     * @param appId 应用id
     * @return 删除结果
     */
    boolean removeByAppId(Long appId);
}
