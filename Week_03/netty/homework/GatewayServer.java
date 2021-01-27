package com.geekbang.shengfq.week2.netty.homework;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * netty作业:gateway1.0
 * @author shengfq
 * @date 2021-01-25
 * */
public class GatewayServer {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);

    private int port;
    private List<String> proxyServer= Arrays.asList("http://localhost:8801","http://localhost:8802");
    public GatewayServer(int port,List<String> proxyServer){
        this.port=port;
        this.proxyServer=proxyServer;
    }

    /**
     * 运行
     * */
    public void run() throws Exception{

        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup(4);
       try {
           ServerBootstrap bootstrap = new ServerBootstrap();
           bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                   .childOption(ChannelOption.SO_KEEPALIVE, true)
                   .childOption(ChannelOption.SO_REUSEADDR, true)
                   .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                   .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                   .childOption(EpollChannelOption.SO_REUSEPORT, true)
                   .childOption(ChannelOption.SO_KEEPALIVE, true)
                   .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
           bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                   .handler(new LoggingHandler(LogLevel.DEBUG))
                   .childHandler(new GatewayServerInitializer(this.proxyServer));

           Channel channel = bootstrap.bind(port).sync().channel();
           logger.info("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
           channel.closeFuture().sync();
       }finally {
           bossGroup.shutdownGracefully();
           workerGroup.shutdownGracefully();
       }
    }

}
