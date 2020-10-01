package com.simplestomp.interceptor;

/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-26
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import sun.net.www.MessageHeader;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <websocke消息监听，用于监听websocket用户连接情况>
 * <功能详细描述>
 *
 * @author wzh
 * @version 2018-08-25 23:39
 * @see [相关类/方法] (可选)
 **/
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    public WebSocketChannelInterceptor() {
    }

    Logger log = LoggerFactory.getLogger(WebSocketChannelInterceptor.class);

    // 在消息发送之前调用，方法中可以对消息进行修改，如果此方法返回值为空，则不会发生实际的消息发送调用
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {
        log.info("WebSocketChannelInterceptor.preSend()...");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("message = " + message + "\n messageChannel = " + messageChannel + "\n accessor=" + accessor);
        /**
         * 1. 判断是否为首次连接请求，如果已经连接过，直接返回message
         * 2. 网上有种写法是在这里封装认证用户的信息，本文是在http阶段，websockt 之前就做了认证的封装，所以这里直接取的信息
         */
        log.info("StompCommand.CONNECT=" + StompCommand.CONNECT);
        log.info("accessor.getCommand() = " + accessor.getCommand());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            /**
             * accessor.getNativeHeader("HEAD_INFO")的作用：
             * 1.获取的是JS stompClient.connect(heads, function (frame){}) 中heads的信息，js中设定为：
             * <code>
             *         var heads={HEAD_INFO:["请求头部信息1","请求头部信息2"]};
             *         stomp.connect(heads, function (frame) {
             * </code>
             * 2. 其中heads参数的key自定义为HEAD_INFO，所以这里取出值对应使用getNativeHeader("HEAD_INFO")，结果为List类型
             * 3. 如果connect的第一个参数通常设置为{},于是使用getNativeHeader("HEAD_INFO")获取一个不存在的key-value会返回null
             */

            List<String> heads = accessor.getNativeHeader("HEAD_INFO");
            log.info("HEAD_INFO="+heads);
            MessageHeaders headMap = accessor.getMessageHeaders();
            log.info("accessor.getMessageHeaders()获取的信息：");
            headMap.forEach((k,v)-> log.info(k+"===="+v));


            /**
             * StompHeaderAccessor accessor的作用
             * 1. accessor中封装了stomp的头部信息，这些信息可以在 @MessageMapping注解的方法中直接带上StompHeaderAccessor
             *    就可以通过方法提供的 accessor.getUser()方法获取到这里封装user对象
             * 2. 可以在这里拿到前端的信息进行登录鉴别
             */
            //UserPrincipal user = (UserPrincipal) accessor.getUser();
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("StompCommand.DISCONNECT="+StompCommand.DISCONNECT);
        }
        return message;
    }

    // 在消息发送后立刻调用，boolean值参数表示该调用的返回值
    @Override
    public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        /*
         * 拿到消息头对象后，我们可以做一系列业务操作
         * 1. 通过getSessionAttributes()方法获取到websocketSession，
         *    就可以取到我们在WebSocketHandshakeInterceptor拦截器中存在session中的信息
         * 2. 我们也可以获取到当前连接的状态，做一些统计，例如统计在线人数，或者缓存在线人数对应的令牌，方便后续业务调用
         */
        HttpSession httpSession = (HttpSession) accessor.getSessionAttributes().get("HTTP_SESSION");

        //获取所有的会话集合
        Map<String, Object> session = accessor.getSessionAttributes();
        log.info("当前会话连接总数："+session.size());
        // 这里只是单纯的打印，可以根据项目的实际情况做业务处理
        log.info("postSend 中获取httpSession key：" + httpSession.getId());

        // 忽略心跳消息等非STOMP消息
        if (accessor.getCommand() == null) {
            return;
        }

        // 根据连接状态做处理，这里也只是打印了下，可以根据实际场景，对上线，下线，首次成功连接做处理
        System.out.println(accessor.getCommand());
        switch (accessor.getCommand()) {
            // 首次连接
            case CONNECT:
                log.info("首次连接httpSession.getId()：" + httpSession.getId());
                break;
            // 连接中
            case CONNECTED:
                log.info("连接中...");
                break;
            // 下线
            case DISCONNECT:
                //当把页面关闭的时候，这里就会提示断开连接
                log.info("断开连接的httpSession.getId()：" + httpSession.getId());
                break;
            default:
                log.info("default...");
                break;
        }


    }

    /*
     * 1. 在消息发送完成后调用，而不管消息发送是否产生异常，在次方法中，我们可以做一些资源释放清理的工作
     * 2. 此方法的触发必须是preSend方法执行成功，且返回值不为null,发生了实际的消息推送，才会触发
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {

    }

    /* 1. 在消息被实际检索之前调用，如果返回false,则不会对检索任何消息，只适用于(PollableChannels)，
     * 2. 在websocket的场景中用不到
     */
    @Override
    public boolean preReceive(MessageChannel messageChannel) {
        return true;
    }

    /*
     * 1. 在检索到消息之后，返回调用方之前调用，可以进行信息修改，如果返回null,就不会进行下一步操作
     * 2. 适用于PollableChannels，轮询场景
     */
    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        return message;
    }

    /*
     * 1. 在消息接收完成之后调用，不管发生什么异常，可以用于消息发送后的资源清理
     * 2. 只有当preReceive 执行成功，并返回true才会调用此方法
     * 2. 适用于PollableChannels，轮询场景
     */
    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {

    }
}


