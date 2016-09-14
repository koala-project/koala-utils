package com.koala.utils.gateway.define;

/**
 * Http api 执行者
 */
public interface HttpApiExecuter {
    void setInstance(Object obj);

    Object execute(String[] parameters);
}
