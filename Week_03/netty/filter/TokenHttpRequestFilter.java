package com.geekbang.shengfq.week2.netty.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求头添加token字符串验证身份
 * @author sheng
 * @date 2021-01-25
 * */
public class TokenHttpRequestFilter implements IHttpRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TokenHttpRequestFilter.class);

    /**
     * 过滤行为
     *
     * @param fullRequest http请求对象
     * @param ctx         上下文对象
     */
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        if(fullRequest!=null){
            logger.info("filter.header ..{}",fullRequest.headers().size());
            fullRequest.headers().set("token","shengfq");
            logger.info("filter.header ..{}",fullRequest.headers().size());
            logger.info("header token:{}",fullRequest.headers().get("token"));
        }
    }
}
