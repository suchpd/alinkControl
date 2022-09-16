package com.alink.control.rabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.alink.control.command.AlinkPositionMessage;
import com.alink.control.configurations.RabbitConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer implements RabbitTemplate.ConfirmCallback {

    //由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 发送alink消息
     * @param message    消息对象
     */
    public void sendAlinkPositionMsg(JSONObject message) {
        //把消息放入ROUTINGKEY对应的队列当中去，对应的是队列
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTINGKEY, message);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
