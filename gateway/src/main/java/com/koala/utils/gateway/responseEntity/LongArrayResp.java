package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("长整形数组返回值")
public class LongArrayResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Description("长整形数组返回值")
    public long[] value;

    public static LongArrayResp convert(long[] ls) {
        LongArrayResp la = new LongArrayResp();
        la.value = ls;
        return la;
    }
}
