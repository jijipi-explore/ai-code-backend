package com.jjp.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-13
 * @Description: 用户登录请求参数
 * @Version: 1.0
 */

@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
