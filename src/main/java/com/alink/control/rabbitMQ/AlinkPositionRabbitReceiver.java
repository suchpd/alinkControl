package com.alink.control.rabbitMQ;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.AlinkPositionMessage;
import com.alink.control.configurations.RabbitConfig;
import com.alink.control.utils.HttpUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.ALINK_POSITION_QUEUE)
public class AlinkPositionRabbitReceiver {

    @RabbitHandler
    public void process(JSONObject message) {
        AlinkPositionMessage messageInfo = message.toJavaObject(AlinkPositionMessage.class);
        JSONObject command = new JSONObject();
        String response;

        //双工非漫游
        if("duplex_non-roaming".equals(messageInfo.getWayOfCommunication())){
            command.put("bid",messageInfo.getBaseStationId());
            command.put("dev_addrs",messageInfo.getDevs());
            command.put("switches",messageInfo.getLedOpen().equalsIgnoreCase("0") ? "01270000" : "01270001");

            response = HttpUtil.postJson(messageInfo.getDomain() + "duplex/sendCommand", JSON.toJSONString(command));

            //双工漫游
        }else if("duplex_roaming".equals(messageInfo.getWayOfCommunication())){
            command.put("dev_addrs",messageInfo.getDevs());
            command.put("switches",messageInfo.getLedOpen().equalsIgnoreCase("0") ? "01270000" : "01270001");

            response = HttpUtil.postJson(messageInfo.getDomain() + "duplex/sendDuplexRoam", JSON.toJSONString(command));

        }else{
            command.put("bid",messageInfo.getBaseStationId());
            command.put("port_pin",messageInfo.getPortPin());
            command.put("devs",messageInfo.getDevs());
            command.put("open",messageInfo.getLedOpen());

            response = HttpUtil.postJson(messageInfo.getDomain() + "duplex/lamps", JSON.toJSONString(command));
        }

        System.out.println("发送命令成功，" + response);
    }
}
