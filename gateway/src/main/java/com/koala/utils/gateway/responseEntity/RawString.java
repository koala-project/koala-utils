package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("返回原始string对象，不进行对象序列化")
public class RawString implements Serializable {
    @Description("要返回的字符串")
    public String value;
}
