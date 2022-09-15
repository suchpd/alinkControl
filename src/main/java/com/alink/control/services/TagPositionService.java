package com.alink.control.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alink.control.utils.CommonUtil;
import com.alink.control.utils.HttpUtil;
import com.alink.control.utils.RedisUtils;
import com.alink.control.utils.SynchronizedHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagPositionService {
    @Value("${alink.base.station.id}")
    private String BASE_STATION_ID;
    @Value("${alink.domain}")
    private String DOMAIN;
    @Value("${alink.way.of.communication}")
    private String Way_Of_Communication;

    private static final String PORT_PIN = "27";
    private final Map<String,String> relationTags;
    private final RedisUtils redisUtils;
    private final SynchronizedHelper synchronizedHelper;

    @Autowired
    public TagPositionService(RedisUtils redisUtils, SynchronizedHelper synchronizedHelper){
        this.redisUtils = redisUtils;
        this.synchronizedHelper = synchronizedHelper;
        this.redisUtils.set("alink_led_open_008012000020","0");
        this.redisUtils.set("alink_led_tags",JSON.toJSONString(Collections.singletonList("008012000020")));
        this.relationTags = new HashMap<String,String>(){{put("000000000334","000000000427");}};
    }

    /**
     * 计算坐标
     */
    public void calculateTheCoordinates(String message){
        if(message.contains("连接成功")){
            return;
        }

        //设备信息及事件
        JSONObject deviceInfo;
        //信标信息
        JSONArray beaconInfos = new JSONArray();

        //判断是否为Json数组，是：定位数据，否：设备信息及事件
        Object object = JSON.parse(message);
        if ( object instanceof JSONArray) {
            beaconInfos = JSONArray.parseArray(message);
        } else if ( object instanceof JSONObject) {
            deviceInfo = JSON.parseObject(message);
            if(deviceInfo.containsKey("MessageType")){
                switch (deviceInfo.get("MessageType").toString()){
                    case "node_status":
                        JSONObject nodeObject = JSON.parseObject(message);
                        if(nodeObject.containsKey("led")){
                            String nodeLed = nodeObject.get("led").toString();
                            if(!"null".equals(nodeLed)){
                                redisUtils.set("alink_led_open_" + nodeObject.get("dev"),"0".equals(nodeLed) ? "0" : "1");
                            }
                            System.out.println("收到传感器信息,led:" + nodeObject.get("led"));
                        }
                        break;
                    case "base_message":
                        System.out.println("收到基站信息");
                        break;
                    case "lost_alarm":
                        System.out.println("设备离线" + message);
                        break;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("类型未知");
        }

        //收到定位消息
        if(beaconInfos.size() >0) {

            for (Object info : beaconInfos) {
                JSONObject tagInfo = JSONObject.parseObject(info.toString());
                String clientId = tagInfo.getString("dev");
                double[] position = {0, 0, 0};
                if (tagInfo.containsKey("x")) {
                    position[0] = Double.parseDouble(tagInfo.getString("x"));
                }
                if (tagInfo.containsKey("y")) {
                    position[1] = Double.parseDouble(tagInfo.getString("y"));
                }
                if (tagInfo.containsKey("z")) {
                    position[2] = Double.parseDouble(tagInfo.getString("z"));
                }

                redisUtils.set("alink_position_" + clientId,position);
            }

            for(Map.Entry<String,String> entry : relationTags.entrySet()){

                String s_position = "alink_position_" + entry.getKey();
                String t_position = "alink_position_" + entry.getValue();

                if(redisUtils.hashKey(s_position) && redisUtils.hashKey(t_position)){
                    controlLed(s_position,t_position,"008012000020");
                }
            }
        }
    }

    /**
     * 控制LED灯
     * @param s_tag  起始标签
     * @param t_tag  目标标签
     */
    private void controlLed(String s_tag,String t_tag,String led_tag){

        double[] s_Coordinate = JSON.parseObject(redisUtils.get(s_tag).toString(),double[].class);
        double[] t_Coordinate = JSON.parseObject(redisUtils.get(t_tag).toString(),double[].class);

        //标签距离
        double distance = CommonUtil.calculateDistance(s_Coordinate,t_Coordinate);
        System.out.println("标签之间距离为：" + distance + "米");

        String ledOpen = distance < 1 ? "1" : "0";
        String ledTagOpenKey = "alink_led_open_" + led_tag;

        if(!ledOpen.equals(redisUtils.get(ledTagOpenKey).toString())){
            redisUtils.set(ledTagOpenKey,ledOpen);

            System.out.println("标签" + ("0".equals(ledOpen) ? "熄灯" : "亮灯"));

            CommonUtil.asyncExecute(()->{
                List<String> devs = JSON.parseObject(redisUtils.get("alink_led_tags").toString(), new TypeReference<List<String>>(){});
                JSONObject ledControlCommand = new JSONObject();

                synchronizedHelper.exec(BASE_STATION_ID,()->{
                    String response;

                    //双工非漫游
                    if("duplex_non-roaming".equals(Way_Of_Communication)){
                        ledControlCommand.put("bid",BASE_STATION_ID);
                        ledControlCommand.put("dev_addrs",devs);
                        ledControlCommand.put("switches",ledOpen.equalsIgnoreCase("0") ? "01270000" : "01270001");

                        response = HttpUtil.postJson(DOMAIN + "duplex/sendCommand", JSON.toJSONString(ledControlCommand));

                    //双工漫游
                    }else if("duplex_roaming".equals(Way_Of_Communication)){
                        ledControlCommand.put("dev_addrs",devs);
                        ledControlCommand.put("switches",ledOpen.equalsIgnoreCase("0") ? "01270000" : "01270001");

                        response = HttpUtil.postJson(DOMAIN + "duplex/sendDuplexRoam", JSON.toJSONString(ledControlCommand));
                    }else{
                        ledControlCommand.put("bid",BASE_STATION_ID);
                        ledControlCommand.put("port_pin",PORT_PIN);
                        ledControlCommand.put("devs",devs);
                        ledControlCommand.put("open",ledOpen);

                        response = HttpUtil.postJson(DOMAIN + "duplex/lamps", JSON.toJSONString(ledControlCommand));
                    }

                    System.out.println("发送指令成功！" + response);
                });
            });
        }


    }
}



