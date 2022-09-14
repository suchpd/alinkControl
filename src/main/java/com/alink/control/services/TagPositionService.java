package com.alink.control.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.ControlLedCommand;
import com.alink.control.utils.CommonUtil;
import com.alink.control.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TagPositionService {
    @Value("${alink.base.station.id}")
    private String BASE_STATION_ID;
    @Value("${alink.domain}")
    private String DOMAIN;

    private final List<String> LED_TAGS = Collections.singletonList("008012000007");
    private final String PORT_PIN = "27";
    private final Map<String,String> LED_TAGS_INFO;
    private final Map<String,String> relationTags;
    private final Map<String,double[]> tagPositions;

    @Autowired
    public TagPositionService(){
        this.LED_TAGS_INFO = new HashMap<String,String>(){{put("008012000007","0");}};
        this.relationTags = new HashMap<String,String>(){{put("000000000334","000000000427");}};
        this.tagPositions  = new HashMap<>();
    }

    /**
     * 计算坐标
     */
    public void calculateTheCoordinates(String message){
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
    private void controlLed(List<String> devs, String open, String port_pin){

        String url = DOMAIN + "duplex/lamps";

        ControlLedCommand controlLedCommand = new ControlLedCommand(BASE_STATION_ID,
                port_pin,
                devs,
                open);

        String response = HttpUtil.postJson(url, JSON.toJSONString(controlLedCommand));
    }
}



