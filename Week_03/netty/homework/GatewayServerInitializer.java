package com.geekbang.shengfq.week2.netty.homework;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * socketChannel初始化
 * @author sheng
 * @date 2021-01-25
 * */
public class GatewayServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServerInitializer.class);
    private List<String> proxyServer;
    public GatewayServerInitializer(List<String> proxyServer){
        logger.info("new GatewayServerInitializer()");
        this.proxyServer=proxyServer;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
      logger.info("channel .... initChannel()");
        ChannelPipeline pipeline=ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        pipeline.addLast(new GatewayServerInboundHandler(proxyServer));
    }
}
