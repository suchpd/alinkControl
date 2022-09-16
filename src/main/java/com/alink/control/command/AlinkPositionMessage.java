package com.alink.control.command;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AlinkPositionMessage {

    private String wayOfCommunication;

    private String baseStationId;

    private String portPin;

    private List<String> devs;

    private String ledOpen;

    private String domain;

//    private JSONObject message;
}
