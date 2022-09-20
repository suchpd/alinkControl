package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("双工非漫游控制Led")
public class ControlLedDuplexNonRoamingCommand {
    @ApiModelProperty(name = "基站序列号sn")
    private String bid;
    @ApiModelProperty(name = "标签Id列表")
    private List<String> dev_addrs;
    @ApiModelProperty(name = "命令")
    private String switches;
}
