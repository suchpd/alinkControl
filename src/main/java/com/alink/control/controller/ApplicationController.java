package com.alink.control.controller;

import com.alibaba.fastjson.JSON;
import com.alink.control.utils.HttpUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApplicationController {

    private static String BASE_STATION_ID = "3000100084";
    private static String DOMAIN="http://localhost:8081/";

    private static Map<String,String> ledMap = new HashMap<>();

    public ApplicationController(){
        ledMap.put("12","008012000012");
        ledMap.put("17","008012000017");
    }

    @PostMapping("controlLED")
    public void controlLED(){


    }

    @PostMapping("test/node_status_p")
    public void nodeStatus_p(String msg){
        System.out.println("node_status_p" + msg);
    }

    @PostMapping("test/base_status_p")
    public void baseStatus_p(String msg){
        System.out.println("base_status_p" + msg);
    }

    @PostMapping("test/base_status_post")
    public void baseStatus_post(String msg){
        System.out.println("base_status_post" + msg);
    }
}


