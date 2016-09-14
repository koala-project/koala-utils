package com.koala.utils.gateway.document;


import com.koala.utils.gateway.annotation.Description;

@Description("调用状态")
public class CallState {
    @Description("返回值")
    public int code;

    @Description("数据长度")
    public int length;

    @Description("返回信息")
    public String msg;
}
