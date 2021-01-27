package com.geekbang.shengfq.week2.netty.homework;

import com.geekbang.shengfq.week2.netty.filter.IHttpRequestFilter;
import com.geekbang.shengfq.week2.netty.filter.TokenHttpRequestFilter;
import com.geekbang.shengfq.week2.netty.router.GatewayHttpRandomRouter;
import com.geekbang.shengfq.week2.netty.router.IGatewayHttpRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 基于事件处理的接入channel处理器
 * @author sheng
 * @date 2021-01-25
 * */
public class GatewayServerInboundHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServerInboundHandler.class);
    private List<String> proxyServer;
    private IHttpRequestFilter filter;
    private IGatewayHttpRouter router;
    private GatewayServerOutboundHandler outboundHandler;

    public GatewayServerInboundHandler(List<String> proxyServer){
        logger.info("new GatewayServerInboundHandler()");
        this.proxyServer=proxyServer;
        this.filter=new TokenHttpRequestFilter();
        this.router=new GatewayHttpRandomRouter();
        this.outboundHandler=new GatewayServerOutboundHandler(proxyServer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead流量接口请求开始，时间为{}",new Date(System.currentTimeMillis()).toString());
        logger.info(" msg:{}",msg.toString());
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        String uri = fullRequest.uri();
        logger.info("接收到的请求url为{}", uri);
        /*
        if(uri.contains("/router")){
            handlerProxy(fullRequest,ctx);
        }*/
        //http请求过滤器实现
        filter.filter(fullRequest,ctx);
        //路由器实现
        outboundHandler.handle(fullRequest,ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("ctx:{}",ctx);
        ctx.flush();
    }


    /**
     * 访问代理服务
     * */
   /* private void handlerProxy(FullHttpRequest fullRequest, ChannelHandlerContext ctx){
        FullHttpResponse response = null;
        try {
            MyHttpClient httpClient=new MyHttpClient();
            String serverUrl=router.route(this.proxyServer);
            String getResult=httpClient.doGet(serverUrl);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(getResult.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            logger.error("处理测试接口出错", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }*/

    /**
     * 简单返回字符串给客户端
     * */
   /* private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            String value = "HELLO";
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            logger.error("处理测试接口出错", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }
*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
