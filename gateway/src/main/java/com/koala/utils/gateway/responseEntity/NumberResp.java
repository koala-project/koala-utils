package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("数值型返回值，包含byte, char, short, int")
public class NumberResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Description("数值型返回值，包含byte, char, short, int")
    public int value;

    public static NumberResp convert(byte n) {
        NumberResp nr = new NumberResp();
        nr.value = n;
        return nr;
    }

    public static NumberResp convert(char n) {
        NumberResp nr = new NumberResp();
        nr.value = n;
        return nr;
    }

    public static NumberResp convert(short n) {
        NumberResp nr = new NumberResp();
        nr.value = n;
        return nr;
    }

    public static NumberResp convert(int n) {
        NumberResp nr = new NumberResp();
        nr.value = n;
        return nr;
    }
}
