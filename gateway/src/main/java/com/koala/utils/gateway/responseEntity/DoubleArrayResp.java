package com.koala.utils.gateway.responseEntity;

import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

@Description("浮点形数组返回值")
public class DoubleArrayResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Description("浮点形数组返回值")
    public double[] value;

    public static DoubleArrayResp convert(double[] ds) {
        DoubleArrayResp da = new DoubleArrayResp();
        da.value = ds;
        return da;
    }

    public static DoubleArrayResp convert(float[] fs) {
        DoubleArrayResp da = new DoubleArrayResp();
        da.value = new double[fs.length];
        for (int i = 0; i < fs.length; i++) {
            da.value[i] = fs[i];
        }
        return da;
    }
}
