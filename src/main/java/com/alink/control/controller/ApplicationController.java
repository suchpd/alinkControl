package com.alink.control.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.*;
import com.alink.control.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Api("埃威定位模块API")
@RestController
public class ApplicationController {

    @Value("${alink.domain}")
    private String DOMAIN;

    public ApplicationController(){
    }

    /**
     * 传感器信息接收接口
     * @param deviceSensorMessage   消息
     */
    @ApiOperation("传感器信息接收接口")
    @PostMapping("test/node_status_post")
    public void nodeStatus_p(@ApiParam("传感器信息") @RequestBody DeviceSensorMessage deviceSensorMessage){
        System.out.println("node_status_p" + deviceSensorMessage);
    }

    /**
     * 基站心跳接收接口
     * @param deviceBaseStationMessage   消息
     */
    @ApiOperation("基站心跳接收接口")
    @PostMapping("test/base_status_post")
    public void baseStatus_p(@ApiParam("基站信息") @RequestBody DeviceBaseStationMessage deviceBaseStationMessage){
        System.out.println("base_status_p" + deviceBaseStationMessage);
    }

    /**
     * 离线消息接收接口
     * @param deviceOfflineMessage   消息
     */
    @ApiOperation("离线消息接收接口")
    @PostMapping("test/lost_alarm_post")
    public void baseStatus_post(@RequestBody DeviceOfflineMessage deviceOfflineMessage){
        System.out.println("base_status_post" + deviceOfflineMessage);
    }

    /**
     * 双工通信控制LED
     * @param controlLedDuplexCommand    请求参数
     * {
     *  "bid":"80000000001",
     *  "port_pin":"27",
     *  "devs":["030000000001","030000000002","030000000003"],
     *  "open": "1"
     * }
     */
    @ApiOperation("控制LED")
    @PostMapping("duplex/lamps")
    public void duplexLamps(@RequestBody ControlLedDuplexCommand controlLedDuplexCommand){
        HttpUtil.postJson(DOMAIN + "duplex/lamps",JSON.toJSONString(controlLedDuplexCommand));
    }

    /**
     * 双工非漫游控制LED
     * @param controlLedDuplexNonRoamingCommand    请求参数
     * {
     *  "bid":"80000000001",
     *  "dev_addrs":["030000000001","030000000002","030000000003"],
     *  "switches": "01270001"
     * }
     */
    @ApiOperation("双工非漫游")
    @PostMapping("duplex/sendCommand")
    public void sendCommand(@RequestBody ControlLedDuplexNonRoamingCommand controlLedDuplexNonRoamingCommand){
        HttpUtil.postJson(DOMAIN + "duplex/sendCommand",JSON.toJSONString(controlLedDuplexNonRoamingCommand));
    }

    /**
     * 双工漫游控制LED
     * @param controlLedDuplexRoamingCommand    请求参数
     * {
     *  "dev_addrs":["030000000001","030000000002","030000000003"],
     *  "switches": "01270001"
     * }
     */
    @ApiOperation("双工漫游")
    @PostMapping("duplex/sendDuplexRoam")
    public void sendDuplexRoam(@RequestBody ControlLedDuplexRoamingCommand controlLedDuplexRoamingCommand){
        HttpUtil.postJson(DOMAIN + "duplex/sendDuplexRoam",JSON.toJSONString(controlLedDuplexRoamingCommand));
    }

    /**
     * 开启/关闭标签过滤功能
     * 默认关闭，开启时过滤所有标签，需要激活标签才可以处理标签信息
     * @param open  是否开启
     */
    @ApiOperation("开启/关闭标签过滤功能")
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
    @ApiOperation("标签激活")
    @GetMapping("setDevStatus")
    public void setDevStatus(String dev,int status){
        HttpUtil.get(DOMAIN + "setDevStatus?dev=" + dev + "?status=" + status);
    }

    /**
     * 批量激活标签
     * @param batchActivateDevCommand   标签信息及工作状态
     */
    @ApiOperation("批量激活标签")
    @PostMapping("logistics/batchActivateDevs")
    public void batchActivateDevs(@RequestBody BatchActivateDevCommand batchActivateDevCommand){
        HttpUtil.postJson(DOMAIN + "logistics/batchActivateDevs", JSON.toJSONString(batchActivateDevCommand));
    }

    /**
     * 标签绑定实体
     * @param tagBindEntityCommand  绑定信息
     */
    @ApiOperation("标签绑定实体")
    @PostMapping("logistics/bind")
    public void bind(@RequestBody TagBindEntityCommand tagBindEntityCommand){
        HttpUtil.postJson(DOMAIN + "logistics/bind", JSON.toJSONString(tagBindEntityCommand));
    }

    /**
     * 解绑
     * @param dev   标签Id
     */
    @ApiOperation("解绑")
    @GetMapping("logistics/unbind")
    public void unbind(String dev){
        HttpUtil.get(DOMAIN + "logistics/unbind?dev=" + dev);
    }

    /**
     * 获取标签绑定状态
     * @param dev   标签Id
     * @return  标签绑定状态
     */
    @ApiOperation("获取标签绑定状态")
    @GetMapping("logistics/getBindStatus")
    public Object getBindStatus(String dev){
        return HttpUtil.get(DOMAIN + "logistics/getbindstatus?dev=" + dev);
    }

    /**
     * 添加/更新标签告警参数
     * @param setTagTypeCommand   标签告警参数
     */
    @ApiOperation("添加/更新标签告警参数")
    @PostMapping("logistics/setType")
    public void setType(@RequestBody SetTagTypeCommand setTagTypeCommand){
        HttpUtil.postJson(DOMAIN + "logistics/setTagTypeCommand", JSON.toJSONString(setTagTypeCommand));
    }

    /**
     * 查询标签告警参数
     * @param typeId    0：查询全部 大于 0：按照 typeId 查询 默认 0
     * @return  标签告警参数
     */
    @ApiOperation("查询标签告警参数")
    @GetMapping("logistics/getType")
    public String getType(int typeId){
        return HttpUtil.get(DOMAIN + "logistics/getType?typeId=" + typeId);
    }

    /**
     * 删除标签告警参数
     * @param typeId    标签类型Id
     */
    @ApiOperation("删除标签告警参数")
    @GetMapping("logistics/deleteType/{typeId}")
    public void deleteType(@PathVariable("typeId")int typeId){
        HttpUtil.get(DOMAIN + "logistics/deleteType/" + typeId);
    }

    /**
     * 切换推送方式
     * @param open  推送方式；1.仅 websocket 2.仅 post 3.全开
     */
    @ApiOperation("设置全局消息推送方式")
    @GetMapping("subscribe")
    public void subscribe(String open){
        HttpUtil.get(DOMAIN + "subscribe?open=" + open);
    }

    /**
     * 查询全局消息推送方式
     * @return  全局消息推送方式
     */
    @ApiOperation("查询全局消息推送方式")
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
     * 查询标签最新传感器状态
     * @param dev   标签Id
     * @return  标签最新传感器状态
     */
    @ApiOperation("查询标签最新传感器状态")
    @GetMapping("logistics/getSensorInfo")
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


