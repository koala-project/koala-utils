package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("浮点形返回值")
public class DoubleResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Description("浮点形返回值")
    public double value;

    public static DoubleResp convert(double d) {
        DoubleResp dr = new DoubleResp();
        dr.value = d;
        return dr;
    }

    public static DoubleResp convert(float d) {
        DoubleResp dr = new DoubleResp();
        dr.value = d;
        return dr;
    }
}
