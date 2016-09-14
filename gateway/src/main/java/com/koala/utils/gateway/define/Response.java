package com.koala.utils.gateway.define;


import com.koala.utils.gateway.annotation.Description;

import java.io.Serializable;

/**
 * Created by Rocky on 2015/12/22.
 */
public class Response<T> implements Serializable {

    @Description("请求状态")
    public Status status;

    @Description("请求结果")
    public T content;

    public Response() {
    }

    public Response(Status status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
