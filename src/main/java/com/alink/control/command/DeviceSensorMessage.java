package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeviceSensorMessage {

    @ApiModelProperty(name = "标签Id")
    private String dev;
    @ApiModelProperty(name = "消息类型")
    private String messageType;
    @ApiModelProperty(name = "标签扩展信息1")
    private String bindId;
    @ApiModelProperty(name = "标签扩展信息2")
    private String bindName;
    @ApiModelProperty(name = "标签电量")
    private String lower;
    @ApiModelProperty(name = "是否正在移动")
    private String move;
    @ApiModelProperty(name = "温度值")
    private String temperature;
    @ApiModelProperty(name = "温度值")
    private String temperature2;
    @ApiModelProperty(name = "气压值")
    private String pressure;
    @ApiModelProperty(name = "LED状态")
    private String led;
}
