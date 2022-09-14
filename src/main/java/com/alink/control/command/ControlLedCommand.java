package com.alink.control.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ControlLedCommand{

    /**
     * 基站序列号 sn
     */
    private String bid;

    /**
     * port_pin="28" 代表红灯
     * port_pin="27" 代表绿灯
     */
    private String port_pin;

    /**
     * 标签 ID 列表（12 位十进制标签 ID）
     */
    private List<String> devs;

    /**
     * "1" 亮; "0" 灭;"2"闪
     */
    private String open;
}
