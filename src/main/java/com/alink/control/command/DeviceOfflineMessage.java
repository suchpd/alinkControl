package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeviceOfflineMessage {
    @ApiModelProperty(name = "消息类型")
    private String messageType;
    @ApiModelProperty(name = "设备Id")
    private String dev;
    @ApiModelProperty(name = "当前时间")
    private String time;
    @ApiModelProperty(name = "当前时间戳")
    private String stamp;
    @ApiModelProperty(name = "丢失时长（s）")
    private int duration;
}
