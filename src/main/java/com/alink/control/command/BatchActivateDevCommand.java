package com.alink.control.command;

import lombok.Data;

import java.util.List;

@Data
public class BatchActivateDevCommand {

    private List<String> devs;

    /**
     * 工作状态，0：下线；1：激活
     */
    private int status;
}
