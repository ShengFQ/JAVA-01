package com.geekbang.shengfq.week2.netty.filter;

import io.netty.handler.codec.http.FullHttpResponse;
/**
 * http响应过滤器
 * @date 2021-01-26
 * @author sheng
 * */
public class TokenHttpResponseFilter implements IHttpResponseFilter {

    private String token;
    public TokenHttpResponseFilter(String token){
        this.token=token;
    }
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("token", token);
    }
}
