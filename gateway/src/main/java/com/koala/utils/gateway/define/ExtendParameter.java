package com.koala.utils.gateway.define;

import java.io.IOException;
import java.util.Properties;

public final class ExtendParameter {

    static Properties props = new Properties();
    static String DEFAULT_TERMINAL = "1";

    public static final String terminal = "_terminal";

    static {
        try {
            props.load(ExtendParameter.class.getResourceAsStream("/terminal.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTerminal(String aid){

        String terminal = props.getProperty(aid);
        if(terminal == null || terminal.equals("")){
            return DEFAULT_TERMINAL;
        }
        return terminal;
    }

    /*
    * params.addQueryStringParameter("appVersion", AndroidUtils.getCurrentAppVersionName(mContext));
        params.addQueryStringParameter("appchannel", AndroidUtils.getChannel(mContext));
        params.addQueryStringParameter("idfa", AndroidUtils.getDeviceId(mContext));
        params.addQueryStringParameter("latitude", CoreApplication.getApp(mContext).getLat() + "");
        params.addQueryStringParameter("longitude", CoreApplication.getApp(mContext).getLon() + "");
        params.addQueryStringParameter("platform", "Android");
        params.addQueryStringParameter("systemVersion", AndroidUtils.getSystemVersion() + "");
        params.addQueryStringParameter("model", Build.MODEL);*/

    public static final String appVersion = "_appVersion";
    public static final String appChannel = "_appchannel";
    public static final String idfa = "_idfa";
    public static final String latitude = "_latitude";
    public static final String longitude = "_longitude";
    public static final String platform = "_platform";
    public static final String systemVersion = "_systemVersion";
    public static final String model = "_model";

}

