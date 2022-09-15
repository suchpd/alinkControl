package com.alink.control.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.BatchActivateDevCommand;
import com.alink.control.command.SetTagTypeCommand;
import com.alink.control.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApplicationController {

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
     * 开启/关闭标签过滤功能
     * 默认关闭，开启时过滤所有标签，需要激活标签才可以处理标签信息
     * @param open  是否开启
     */
    @GetMapping("pushnode_status")
    public void pushNodeStatus(String open){
        HttpUtil.get(DOMAIN + "pushnode_status?open=" + open);
    }

    /**
     * 标签激活
     * 开启标签过滤功能后，需要激活标签，默认所有标签均属于下线状态
     * @param dev  标签Id
     * @param status  工作状态
     */
    @GetMapping("setDevStatus")
    public void setDevStatus(String dev,int status){
        HttpUtil.get(DOMAIN + "setDevStatus?dev=" + dev + "?status=" + status);
    }

    /**
     * 批量激活标签
     * @param batchActivateDevCommand   标签信息及工作状态
     */
    @PostMapping("batchActivateDevs")
    public void batchActivateDevs(@RequestBody BatchActivateDevCommand batchActivateDevCommand){
        HttpUtil.postJson(DOMAIN + "logistics/batchActivateDevs", JSON.toJSONString(batchActivateDevCommand));
    }

    /**
     * 添加/更新标签告警参数
     * @param setTagTypeCommand   标签告警参数
     */
    @PostMapping("setType")
    public void setType(@RequestBody SetTagTypeCommand setTagTypeCommand){
        HttpUtil.postJson(DOMAIN + "logistics/setTagTypeCommand", JSON.toJSONString(setTagTypeCommand));
    }

    /**
     * 查询标签告警参数
     * @param typeId    0：查询全部 大于 0：按照 typeId 查询 默认 0
     * @return  标签告警参数
     */
    @GetMapping("getType")
    public String getType(int typeId){
        return HttpUtil.get(DOMAIN + "logistics/getType?typeId=" + typeId);
    }

    /**
     * 删除标签告警参数
     * @param typeId    标签类型Id
     */
    @GetMapping("deleteType/{typeId}")
    public void deleteType(@PathVariable("typeId")int typeId){
        HttpUtil.get(DOMAIN + "logistics/deleteType/" + typeId);
    }

    /**
     * 切换推送方式
     * @param open  推送方式；1.仅 websocket 2.仅 post 3.全开
     */
    @GetMapping("subscribe")
    public void subscribe(String open){
        HttpUtil.get(DOMAIN + "subscribe?open=" + open);
    }

    /**
     * 查询全局消息推送方式
     * @return  全局消息推送方式
     */
    @GetMapping("getSubscribeType")
    public String getSubscribeType(){
        JSONObject response = JSON.parseObject(HttpUtil.get(DOMAIN + "getSubscribeType"));

        if(!response.containsKey("code")){
            return response.toString();
        }

        if(!response.get("code").equals(200)){
            return "查询失败！";
        }

        String result = response.get("data").toString();
        return "1".equals(result) ? "仅Websocket" : "2".equals(result) ? "仅Post（Http）" : "3".equals(result) ? "全开" : "";
    }

    /**
     * 插叙标签最新传感器状态
     * @param dev   标签Id
     * @return  标签最新传感器状态
     */
    @GetMapping("getSensorInfo")
    public Object getSensorInfo(String dev){
        JSONObject response = JSON.parseObject(HttpUtil.get(DOMAIN + "logistics/getSensorInfo?dev=" + dev));

        if(!response.containsKey("code")){
            return response;
        }

        if(!response.get("code").equals(200)){
            return JSON.parseObject("查询失败！");
        }

        return response.get("data");
    }
}


