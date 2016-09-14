package com.koala.utils.gateway.define;

/**
 * Created by Rocky on 2015/12/22.
 */
public class Responses {

    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MSG = "请求成功";

    public static Status DEFAULT_SUCCESS_STATUS = new Status(SUCCESS_CODE,SUCCESS_MSG, System.currentTimeMillis());

    @SuppressWarnings("rawtypes")
	public static <T extends Response> T success(Class<T> clazz){
    	try {
			T response = clazz.newInstance();
			response.setStatus(DEFAULT_SUCCESS_STATUS);
			return response;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
    	return null;
    }
}
