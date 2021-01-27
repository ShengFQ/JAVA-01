package com.geekbang.shengfq.week2.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * netty实现的简单http服务
 * 验证Reactor的三种线程模式的有优点
 * 验证TCP参数
 * 验证ChannelHandler的业务处理
 * 验证并发处理能力
 *
 * */
public class HttpHelloWorldServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
    public static void main(String[] args) throws Exception {
        //singleThreadReactor();
        multiThreadReactor();
       // masterReactor();
    }

    /**
     * 主从模式运行Reactor
     * */
    private static void masterReactor() throws Exception{
        httpServer(0,0);
    }
    /**
     * 单线程运行Reactor
     * */

    private static void singleThreadReactor() throws Exception{
        httpServer(0,-1);
    }
    /**
     * 多线程运行Reactor
     * */
    private static void multiThreadReactor()throws Exception{
        httpServer(1,0);
    }
    private static void httpServer(int boss,int worker) throws Exception{
        EventLoopGroup bossGroup=null;
        EventLoopGroup workerGroup=null;
        if(boss>=0) {
            bossGroup = new NioEventLoopGroup(boss);
        }
        if(worker>=0) {
            workerGroup = new NioEventLoopGroup(worker);
        }

        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            if(boss==0 && worker==-1){
                bootstrap.group(bossGroup, bossGroup);
            }else {
                bootstrap.group(bossGroup, workerGroup);
            }
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.childHandler(new HttpHelloWorldServerInitializer(sslCtx));
            Channel channel = bootstrap.bind(PORT).sync().channel();
            System.out.println("浏览器访问 http://127.0.0.1:" + PORT + "/");
            channel.closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(bossGroup!=null) {
                bossGroup.shutdownGracefully();
            }
            if(workerGroup!=null) {
                workerGroup.shutdownGracefully();
            }
        }
    }
}
