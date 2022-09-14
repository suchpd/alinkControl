package com.alink.control.controller;

import com.alink.control.command.HttpSensorMessageCommand;
import com.alink.control.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApplicationController {

    @Value("${alink.base.station.id}")
    private static String BASE_STATION_ID;
    @Value("${alink.domain}")
    private String DOMAIN;

    public ApplicationController(){
    }

    /**
     * 定位信息接收接口
     * @param msg   消息
     */
    @PostMapping("test/position_post")
    public void position_p(@RequestBody String msg){
        System.out.println("position_p" + msg);
    }

    /**
     * 传感器信息接收接口
     * @param msg   消息
     */
    @PostMapping("test/node_status_post")
    public void nodeStatus_p(@RequestBody String msg){
        System.out.println("node_status_p" + msg);
    }

    /**
     * 基站心跳接收接口
     * @param msg   消息
     */
    @PostMapping("test/base_status_post")
    public void baseStatus_p(@RequestBody String msg){
        System.out.println("base_status_p" + msg);
    }

    /**
     * 离线消息接收接口
     * @param msg   消息
     */
    @PostMapping("test/lost_alarm_post")
    public void baseStatus_post(@RequestBody String msg){
        System.out.println("base_status_post" + msg);
    }

    /**
     * 切换推送方式
     * @param open  推送方式；1.仅 websocket 2.仅 post 3.全开
     */
    @GetMapping("subscribe")
    public void subscribe(String open){
        HttpUtil.get(DOMAIN + "subscribe?open=" + open);
    }
}


