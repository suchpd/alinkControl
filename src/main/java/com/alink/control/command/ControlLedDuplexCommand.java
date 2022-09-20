package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("双工通信控制Led")
public class ControlLedDuplexCommand {
    @ApiModelProperty(name = "基站序列号sn")
    private String bid;
    @ApiModelProperty(name = "GPIO引脚，27：绿灯，28：红灯")
    private String port_pin;
    @ApiModelProperty(name = "标签Id列表")
    private List<String> devs;
    @ApiModelProperty(name = "1：亮，0：灭")
    private String open;
}
