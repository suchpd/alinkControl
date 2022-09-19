package com.alink.control.command;

import lombok.Data;

@Data
public class TagBindEntityCommand {

    /**
     * 标签id
     */
    private String dev;

    /**
     * 标签类型Id
     */
    private String devType;

    /**
     * 绑定实体Id
     */
    private String bindId;

    /**
     * 绑定实体名称
     */
    private String bindName;

    /**
     * 标签可在区域Id
     */
    private String acceptedRegion;

    /**
     * 标签禁止区域Id
     */
    private String forbiddenRegion;
}
