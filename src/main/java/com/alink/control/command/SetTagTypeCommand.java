package com.alink.control.command;

import lombok.Data;

@Data
public class SetTagTypeCommand {

    /**
     *   1 灵巧性物资，2 长续航物资，3 人员,4 开始；可自定义 默认 2 必须大于 0
     */
    private Integer typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 信号丢失报警时间
     */
    private Integer lostDuration;

    /**
     * 关联告警次数
     */
    private int work;
}
