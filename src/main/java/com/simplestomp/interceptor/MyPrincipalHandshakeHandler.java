package com.simplestomp.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;


/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-26
 */
//设置认证用户信息
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler{

    private static final Logger log = LoggerFactory.getLogger(MyPrincipalHandshakeHandler.class);


    /**
     * 该方法在建立过程中把一个user与WebSocket session关联起来
     * 该方法如果不覆盖，会默认调用{@link ServerHttpRequest#getPrincipal()}，这意味着要提供一个Principal接口的实现
     * @param request ServerHttpRequest类型
     * @param wsHandler WebSocketHandler类型
     * @param attributes Map类型，传递给WebSocket会话的属性
     * @return 返回与WebSocket会话的用户信息（封装在Principal接口中),如果返回null则该会话不可用
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("MyPrincipalHandshakeHandler determineUser()...");
        log.info("ServerHttpRequest request="+request.getHeaders());
        log.info("WebSocketHandler wsHandler="+wsHandler.supportsPartialMessages());

        //获取一个session会话，在这里如果session会话不存在则不会创建
        HttpSession httpSession = this.getSession(request);

        // 获取首次登录时提供的信息，假设这里的token已经保存到session会中
        //对于两个一对一发送消息的用户来说，token值是必要的
        String token = (String) httpSession.getAttribute("token");
        if(StringUtils.isEmpty(token)){
            log.warn("会话中token值为空，没有登录websocket，这里使用一个默认值：101!");
            token = "101";
            //实际使用一对一通讯，当token为空，要返回null，这里是为了测试所以随便取个默认值来放行
            //return null;
        }
        log.info("token = " + token);
        /**
         * Principal的作用
         * 1.使用UserPrincipal实现了Principal接口，在一对一通讯中，这是发送方与接收方联系的依据
         * 2.这里的构造参数token，指定了发送方法发送方式：
         * <code> stomp.send("/app/clientToClient", {}, payload);</code>
         * 接收方必须以这样的方式来接收消息：
         * <code>stomp.subscribe("/user/{token}/queue", function (response)</code>
         * 3.两个用户一对一通讯，接收方要先获取token才能使用subscribe，假设token="101",则接收方法的请求则为：
         * /user/101/queue
         * 4.配置路径要求
         * 这里的user必须被配置（默认配置/user,换为其他必须显式指出）：setUserDestinationPrefix("/user");
         * 这里的queue也必须包含在配置中：registry.enableSimpleBroker("/user","/topic","/users","/queue");
         * 5.Controller处理方法对应使用
         *     @MessageMapping("/clientToClient")
         *     @SendToUser(value ="/queue",broadcast = false)
         */

        Principal principal = new UserPrincipal(token);
        return principal;
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }

}

