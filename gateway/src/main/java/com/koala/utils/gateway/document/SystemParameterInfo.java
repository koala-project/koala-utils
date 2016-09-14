package com.koala.utils.gateway.document;


import com.koala.utils.gateway.annotation.Description;

@Description("系统级参数")
public class SystemParameterInfo {
    @Description("参数名称")
    public String name;
    @Description("描述")
    public String desc;
}
