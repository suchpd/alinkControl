package com.alink.control.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeviceBaseStationMessage {
    @ApiModelProperty(name = "消息类型")
    private String messageType;
    @ApiModelProperty(name = "基站序列号sn")
    private String bid;
    @ApiModelProperty(name = "基站每秒接受标签广播数量")
    private String rps;
    @ApiModelProperty(name = "基站每秒发送标签广播数量")
    private String tps;
}
