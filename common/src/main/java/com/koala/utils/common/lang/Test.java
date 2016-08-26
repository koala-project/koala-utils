package com.koala.utils.common.lang;

/**
 * @Author Liuyf
 * @Date 2016-08-15
 * @Time 12:01
 * @Description
 */

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Test implements Serializable {

    private static final long serialVersionUID = 2640934692335200272L;

    public static void main(String[] args) {

        // data segment
        Integer[] SAMPLE_ARRAY = new Integer[] { 1,2,3 };
        Integer[] SAMPLE_ARRAY1 = new Integer[] { 2,3,4};
        List<Integer> NEW_ARRAY = Arrays.asList(SAMPLE_ARRAY);
        List<Integer> NEW_ARRAY1 = Arrays.asList(SAMPLE_ARRAY1);
        System.out.println(NEW_ARRAY.containsAll(NEW_ARRAY1));

    }

}
