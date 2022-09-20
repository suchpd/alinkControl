package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DevicePositionMessage {
    @ApiModelProperty(name = "标签Id")
    private String dev;
    @ApiModelProperty(name = "x轴坐标")
    private String x;
    @ApiModelProperty(name = "y轴坐标")
    private String y;
    @ApiModelProperty(name = "z轴坐标")
    private String z;
    @ApiModelProperty(name = "标签扩展信息1")
    private String bindId;
    @ApiModelProperty(name = "标签扩展信息2")
    private String bindName;
    @ApiModelProperty(name = "标签类型")
    private int devType;
    @ApiModelProperty(name = "推送时间")
    private String pushTime;
    @ApiModelProperty(name = "推送时间戳")
    private String pushTimeStamp;
    @ApiModelProperty(name = "地图Id")
    private String mapId;
}
