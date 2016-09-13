package com.koala.utils.common.lang;

import java.math.BigDecimal;

/**
 * 用于计算数据的工具类
 */
public class CalculateUtil {

	public static Double round(Double value, int newScale, int roundingMode){
		if(value == null)
			return null;
		BigDecimal bg = new BigDecimal(0);  
		bg = bg.valueOf(value);
        value = bg.setScale(newScale, roundingMode).doubleValue();
		return value;
	}

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (v2 == 0 || scale < 0) return 0;
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double mul(double d1, double d2)
    {        // 进行乘法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    public static double round(double d, int len) {     // 进行四舍五入操作
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len,BigDecimal. ROUND_HALF_UP).doubleValue();
    }
}
