package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("字符串返回值")
public class StringResp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Description("字符串返回值")
    public String value;

    public static StringResp convert(String s) {
        StringResp sr = new StringResp();
        sr.value = s;
        return sr;
    }
}
