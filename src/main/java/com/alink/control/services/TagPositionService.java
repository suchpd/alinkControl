package com.alink.control.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.ControlLedCommand;
import com.alink.control.utils.CommonUtil;
import com.alink.control.utils.HttpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagPositionService {
    private static final String BASE_STATION_ID = "3000100084";
    private static final String DOMAIN="http://localhost:8081/";
    private static final List<String> LED_TAGS = Collections.singletonList("008012000007");
    private static final String PORT_PIN = "27";
    private static final Map<String,String> LED_TAGS_INFO = new HashMap<>();
    private static final Map<String,String> relationTags = new HashMap<>();
    private static Map<String,double[]> tagPositions  = new HashMap<>();
    private static LocalDateTime lastControlLedDate = LocalDateTime.now();

    @PostConstruct
    public void init(){
        LED_TAGS_INFO.put("008012000007","0");
        relationTags.put("000000000334","000000000427");
    }

    /**
     * 计算坐标
     */
    public static void calculateTheCoordinates(String message){
        if(message.contains("连接成功")){
            return;
        }
        JSONObject baseStationInfo = new JSONObject();
        JSONArray tagInfos = new JSONArray();

        Object object = JSON.parse(message);
        if ( object instanceof JSONObject) {
            baseStationInfo = JSON.parseObject(message);
        } else if ( object instanceof JSONArray) {
            tagInfos = JSONArray.parseArray(message);
        } else {
            System.out.println("类型未知");
        }

        if(tagInfos.size() >0) {

            for (Object info : tagInfos) {
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

                if (tagPositions.containsKey(clientId)) {
                    tagPositions.replace(clientId, position);
                } else {
                    tagPositions.put(clientId, position);
                }
            }

            for(Map.Entry<String,String> entry : relationTags.entrySet()){
                if(tagPositions.containsKey(entry.getKey()) && tagPositions.containsKey(entry.getValue())){

                    double distance = CommonUtil.calculateDistance(tagPositions.get(entry.getKey()),tagPositions.get(entry.getValue()));

                    System.out.println("标签之间距离为：" + distance + "米");

                    String ledOpen = "0";
                    if(distance < 1){
                        ledOpen = "1";
                    }

                    if(!ledOpen.equals(LED_TAGS_INFO.get("008012000007"))){
                        LED_TAGS_INFO.replace("008012000007",ledOpen);
                        lastControlLedDate =  LocalDateTime.now();
//                        controlLed(Collections.singletonList(LED_TAGS.get(0)),LED_TAGS_INFO.get("008012000007"),PORT_PIN);

                        CommonUtil.asyncExecute(()->{
                            controlLed(Collections.singletonList(LED_TAGS.get(0)),LED_TAGS_INFO.get("008012000007"),PORT_PIN);
                        });
                    }
                }
            }
        }
    }

    /**
     * 控制LED灯
     * @param devs  led标签Id
     * @param open  是否亮灯
     * @param port_pin  亮灯颜色
     */
    private static void controlLed(List<String> devs, String open, String port_pin){

        String url = DOMAIN + "duplex/lamps";

        ControlLedCommand controlLedCommand = new ControlLedCommand(BASE_STATION_ID,
                port_pin,
                devs,
                open);
        controlLedCommand.setBid(BASE_STATION_ID);

        HttpUtil.postJson(url, JSON.toJSONString(controlLedCommand));
    }
}



