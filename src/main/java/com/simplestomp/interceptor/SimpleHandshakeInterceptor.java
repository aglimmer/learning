package com.simplestomp.interceptor;

import com.simplestomp.controller.StompController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;
/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-26
 * WebSocket建立连接前后的拦截器
 */

public class SimpleHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private Logger log = LoggerFactory.getLogger(SimpleHandshakeInterceptor.class);

    /**
     *  beforeHandshake在ServerSocket握手前调用：
     *  1.这里主要用于在Map参数中添加一些用户认证信息供后续验证使用
     *  2.这里覆盖了父类的方法添加新的内容，最后还需要调用父类方法
     * @return true 表示正常处理
     * @return false 表示拒绝握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map)
            throws Exception {
        log.info("beforeHandshake()...");
        log.info(" WebSocketHandler webSocketHandler="+webSocketHandler);
        ServletServerHttpRequest servletRequset = (ServletServerHttpRequest) request;
        //这里的Principal接口获取为null，后序要实现用户一对一传输需要实现Principal接口
        //log.info("principal="+request.getPrincipal());
        //map是一个空对象，可用来在该方法内部可以自定义添加一些map键值信息
        //System.out.println("map="+map);

        /**
         * getSession(false)说明：
         * 1.当从request中获取不到session那就不创建，返回null
         * 2.通常在发起服务器首次请求时，getSession(false)返回null
         * 3.创建连接之前在一个简单的init.html页面使用请求 /initconnect 方法
         * {@link StompController#initConnect(java.lang.String, javax.servlet.http.HttpSession)}
         * 该方法创建了HTTPSession对象，所以这里getSession(false)能够返回已创建的非null的session
         * 4.倘若首次请求没有先访问请求 /initconnect，这里改为getSession()
         */
        //HttpSession httpSession = servletRequset.getServletRequest().getSession(false);

        //getSession()返回一个与这个请求相关的当前的session，如果当前请求还没有session，则会进行创建
        HttpSession httpSession = servletRequset.getServletRequest().getSession();
        /**
         * 这里在会话中保存一个token，当继续执行到以下方法：
         * {@link WebSocketChannelInterceptor#postSend(
         * org.springframework.messaging.Message, org.springframework.messaging.MessageChannel, boolean)}
         * 就可以在方法内部使用<code>httpSession.getAttribute("token")</code>获取token的值，然后将token设置到
         * Principal接口的实现类中
         */
        //假如这里固定token
        //httpSession.setAttribute("token","101");

        System.out.println("httpSession="+httpSession);
        if (httpSession != null) {
            // 对于一个不为null的httpSession，使用getId()获取具体值
           log.info("httpSession.getId()：" + httpSession.getId());
            // 获取到httpsession后，可以根据自身业务，操作其中的信息，这里只是单纯的和websocket进行关联
            map.put("HTTP_SESSION", httpSession);
        } else {
            log.warn("httpSession is null!");
        }
        /**
         * 调用父类方法的作用：
         * 1.第4个参数Map保存HTTP与HTTP握手关联的会话属性
         * 2.调用父类方法后，默认会把这些属性进行复制以供后序使用，当前map不可用
         */
        return super.beforeHandshake(request, response, webSocketHandler, map);
    }

    /**
     * websocket握手建立后调用
     *
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        log.info("websocket连接握手成功");
    }
}
