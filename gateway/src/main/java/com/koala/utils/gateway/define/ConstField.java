package com.koala.utils.gateway.define;

import java.nio.charset.Charset;

public class ConstField {
    public static final Charset UTF8                 = Charset.forName("utf-8");
    public static final Charset ASCII                = Charset.forName("ascii");
    public static final byte[]  XML_START            = "<xml>".getBytes(UTF8);
    public static final byte[]  XML_END              = "</xml>".getBytes(UTF8);
    public static final byte[]  JSON_START           = "{\"stat\":".getBytes(UTF8);
    public static final byte[]  JSON_CONTENT         = ",\"content\":[".getBytes(UTF8);
    public static final byte[]  XML_EMPTY            = "<empty/>".getBytes(ConstField.UTF8);
    public static final byte[]  JSON_SPLIT           = ",".getBytes(UTF8);
    public static final byte[]  JSON_END             = "]}".getBytes(UTF8);
    public static final byte[]  JSON_EMPTY           = "{}".getBytes(UTF8);
    public static final byte[]  JSONP_START          = "(".getBytes(UTF8);
    public static final byte[]  JSONP_END            = ");".getBytes(UTF8);
    public static final String ERROR_CODE_EXT       = "com.ulife.common.gateway.utils.ERROR_CODE_EXT";
    public static final String SET_COOKIE_TOKEN     = "com.ulife.common.gateway.utils.SET_COOKIE_TOKEN";
    public static final String SET_COOKIE_USER_INFO = "com.ulife.common.gateway.utils.SET_COOKIE_USER_INFO";
    public static final String REDIRECT_TO          = "com.ulife.common.gateway.utils.REDIRECT_TO";
    public static final String CREDIT               = "com.ulife.common.gateway.utils.CREDIT";
    public static final String MSG                  = "com.ulife.common.gateway.utils.MSG";
    public static final String SERVICE_LOG          = "com.ulife.common.gateway.utils.SERVICE_LOG";
}

