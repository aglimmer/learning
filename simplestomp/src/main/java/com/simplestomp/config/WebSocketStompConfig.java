package com.simplestomp.config;


import com.simplestomp.interceptor.SimpleHandshakeInterceptor;
import com.simplestomp.interceptor.WebSocketChannelInterceptor;
import com.simplestomp.interceptor.MyPrincipalHandshakeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-24
 */


@Configuration
//配置类以启用代理支持，可以在WebSocket上使用更高级的消息传递子协议进行消息传递
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer{

    //注册STOMP协议的节点,并映射指定的url
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //设置指定的路径启用SockJS功能
//        registry.addEndpoint("/simple").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/simple")
                .setAllowedOrigins("*")
                .addInterceptors(new SimpleHandshakeInterceptor())
                .setHandshakeHandler(new MyPrincipalHandshakeHandler())
                .withSockJS();
    }
    //配置消息代理
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry)
    {
        //自定义路径前缀，使其能够向客户端发送消息
        registry.enableSimpleBroker("/user","/topic","/users","/queue");
        //自定义客户端向服务端发起请求时，需要以/app为前缀
        registry.setApplicationDestinationPrefixes("/app");
        //给指定用户发送一对一的消息前缀是/user，不指定也是默认/user
        //setUserDestinationPrefix配置参数必须保证在enableSimpleBroker也存在，否则配置不成功
        registry.setUserDestinationPrefix("/user");
    }


    /**
     *设置输出消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     * @param registration
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
    }
    /**
     * 拦截器加入 spring ioc容器
     * @return
     */
    @Bean
    public WebSocketChannelInterceptor webSocketChannelInterceptor()
    {
        return new WebSocketChannelInterceptor();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        /*
         * 配置消息线程池
         * 1. corePoolSize 配置核心线程池，当线程数小于此配置时，不管线程中有无空闲的线程，都会产生新线程处理任务
         * 2. maxPoolSize 配置线程池最大数，当线程池数等于此配置时，不会产生新线程
         * 3. keepAliveSeconds 线程池维护线程所允许的空闲时间，单位秒
         */
        //设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
        /*
         * 添加stomp自定义拦截器，可以根据业务做一些处理
         * springframework 4.3.12 之后版本此方法废弃，代替方法 interceptors(ChannelInterceptor... interceptors)
         * 消息拦截器，实现ChannelInterceptor接口
         */
        registration.interceptors(this.webSocketChannelInterceptor());
    }

}