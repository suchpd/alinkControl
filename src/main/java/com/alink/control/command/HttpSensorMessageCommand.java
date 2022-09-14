package com.alink.control.command;

import lombok.Data;

@Data
public class HttpSensorMessageCommand {
    private String dev;

    private String MessageType;

    private String bindId;

    private String bindName;

    private String lower;

    private String move;

    private String temperature;

    private String temperature2;

    private String pressure;

    private String led;
}
