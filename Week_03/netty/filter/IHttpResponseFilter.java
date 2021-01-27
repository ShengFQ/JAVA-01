package com.geekbang.shengfq.week2.netty.filter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 过滤器接口,过滤器的参数定义
 * @date 2021-01-25
 * */
public interface IHttpResponseFilter {
    /**
     * 过滤行为
     * @param response http响应对象
     * */
    void filter(FullHttpResponse response);
}
