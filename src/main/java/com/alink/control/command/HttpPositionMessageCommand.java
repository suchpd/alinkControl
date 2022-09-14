package com.alink.control.command;

import lombok.Data;

@Data
public class HttpPositionMessageCommand {
    private String dev;

    private String x;

    private String y;

    private String z;

    private String bindId;

    private String bindName;

    private String devType;

    private String pushTime;

    private String pushTimeStamp;

    private String mapId;
}
