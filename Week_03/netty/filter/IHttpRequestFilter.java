package com.geekbang.shengfq.week2.netty.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
/**
 * 过滤器接口,过滤器的参数定义
 * @date 2021-01-25
 * */
public interface IHttpRequestFilter {
    /**
     * 过滤行为
     * @param fullRequest http请求对象
     * @param ctx  上下文对象
     * */
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
}
