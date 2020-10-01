package com.simplestomp.controller;

import com.simplestomp.model.Information;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;

//import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-24
 */
@Controller
//@RequestMapping("/stomp")
@EnableScheduling
public class StompController {
    private Logger log = LoggerFactory.getLogger(StompController.class);
    /**
     * Spring内置SimpMessagingTemplate
     * 能够在应用的任何地方发送消息，不必以首先接收一条消息作为前提
     * 最简单的使用方式是将它（或者其接口SimpMessageSendingOperations）自动装配到所需的对象中：
     *
     * @Autowired private SimpMessagingTemplate simpMessagingTemplate;
     * SimpMessagingTemplate还提供convertAndSendToUser()方法,能够让给特定用户发送消息
     */
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * @return  Infomation对象
     * @SubscribeMapping注解： 用于js方法subscribe()发送的请求
     * stomp.subscribe('/app/subscribe', function(response)...
     */
    @SubscribeMapping("/handleSubscribe")
    public Information handleSubscribe() {
        log.info("handleSubscribe...");
        Information outing = new Information();
        outing.setMessage("subscribes");
        return outing;
    }


    /**
     * 一对一发送消息配置
     * 详见：
     * {@link com.simplestomp.interceptor.MyPrincipalHandshakeHandler#determineUser(org.springframework.http.server.ServerHttpRequest, org.springframework.web.socket.WebSocketHandler, java.util.Map)}
     */
    @MessageMapping("/clientToClient")
    @SendToUser(value ="/queue",broadcast = false)
    public Information clientToClient(Information obj, Principal principal, StompHeaderAccessor headerAccessor) {
        log.info("clientToClient(),con = " + obj);
        log.info("principal = " + principal.getName());
        log.info("headerAccessor="+headerAccessor);

//        this.simpMessageSendingOperations.convertAndSendToUser("1", "/obj", obj);
        return obj;
    }

    /**
     * 请求时参数httpSession会被创建，用于保存用户请求信息到httpSession会话中
     * @param token
     * @param httpSession
     * @return
     */
    @ResponseBody
    @GetMapping("/initConnect")
    public String initConnect(@RequestParam("token") String token, HttpSession httpSession){
        System.out.println("initConnect()...");
        System.out.println("token = " + token);
        httpSession.setAttribute("token",token);
        return "首次请求服务器成功！";
    }
    /**
     * 给指定的用户推送消息
     * 1.<code>convertAndSendToUser("101", "/detail", obj)
     *  js消息发送请求使用：stomp.send("/app/userToUser", {}, payload);
     *  js目标用户接受消息：stomp.subscribe("/user/101/detail", function (response) {
     * 2.一般推荐使用convertAndSendToUser设置推送消息的目标用户
     * @param obj
     */
    @MessageMapping("/userToUser")
    public void userToUser(Information obj) {
        System.out.println("toAnother(),obj = " + obj);
        this.simpMessageSendingOperations.convertAndSendToUser("101", "/detail", obj);
    }


    /**
     * 相当于实现广播推送消息
     *
     * @MessageMapping("/message")要能被映射：
     * 1.在配置类中使用：registry.setApplicationDestinationPrefixes("/app")，
     * 表示请求要以/app为前缀的请求才会被映射到@MessageMapping注解的方法上
     * 2.在javascript发起请求时使用： stomp.send("/app/message", {}, payload);
     * 3.此时 /message才会被响应
     *
     * @SendTo("/users/one")要能被映射：
     * 1.配置类中使用：registry.enableSimpleBroker("/users","/clients");
     * 2.在javascript中订阅消息时使用：stomp.subscribe('/users/one', function (response)...
     */
    @MessageMapping("/toAllUser")
    @SendTo("/users/message")
    public Information toAllUser(Information content) {
        System.out.println("toAllUser(),content=" + content.getMessage());
        //返回对象会转化为json数据类型
        return content;
    }

    /**
     * 发送一个普通请求 /sendMessage, 实现广播消息和推送到指定用户
     */
    @ResponseBody
    @RequestMapping("/sendMessage")
    public String sendMessage() {
        String msg = "/sendMessage请求的测试消息";
        System.out.println(msg);
        /**
         * convertAndSend方法：
         * 1.javascript请求路径使用：stomp.subscribe('/users/one', function (response) {...
         * 2.相当于广播功能
         */
        simpMessageSendingOperations.convertAndSend("/users/message", msg);

        /**
         * convertAndSendToUser方法：
         * 1.给指定用户发送消息，三个参数分别是：用户id，目标路径,传递参数
         * 2.javascript使用
         *  <code>stomp.subscribe('/user/101/detail', function (response) {...</code>
         * 3.这里的路径需要在配置类设置,否则无法转发消息
         * <code>
         *     registry.enableSimpleBroker("/user");
         *     registry.setUserDestinationPrefix("/user");
         * </code>
         */
        simpMessageSendingOperations.convertAndSendToUser("101", "/detail", msg);
        return "send success!";
    }

    /**
     * 使用广播定时推送消息
     * @Scheduled注解:
     * 该注解用在一个方法上, 要让该注解生效，需要在类上使用注解@EnableScheduling，表示启用时间计划
     */
    @Scheduled(fixedRate = 6000)
    public void sendTopicMessage() {
        //Information content = new Information();
        String message = "当前时间：" + LocalDateTime.now().toLocalTime().toString();
        //content.setMessage(message);
        //广播推送消息
        this.simpMessageSendingOperations.convertAndSend("/users/message", "广播消息："+message);
    }
    @ResponseBody
    @PostMapping("/users/{username}")
    public String initRequest(@PathVariable("username") String username,HttpSession session) {
        log.info("initRequest(),username="+username);
        if (!StringUtils.isEmpty(username)) {
            session.setAttribute("token",username);
        }
        return "token设置成功！";
    }

}
