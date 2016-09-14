package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("返回json格式的string")
public class JSONString implements Serializable {
    @Description("json string")
    public String value;
}
