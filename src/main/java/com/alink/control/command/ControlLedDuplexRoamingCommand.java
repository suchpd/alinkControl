package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("双工漫游控制Led")
public class ControlLedDuplexRoamingCommand {
    @ApiModelProperty(name = "标签Id列表")
    private List<String> dev_addrs;
    @ApiModelProperty(name = "命令")
    private String switches;
}
