package com.simplestomp.interceptor;

/**

 */
import java.security.Principal;

/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-26
 * 用户一对一连接这是必要的，用于保存user对象
 **/
public class UserPrincipal implements Principal{
    /**
     * 用户身份标识符
     */
    private String token;
    public UserPrincipal() { }

    public UserPrincipal(String token) {
        this.token = token;
    }

    /**
     * 获取用户登录令牌
     * @return
     */
    @Override
    public String getName() {
        return token;
    }
}
