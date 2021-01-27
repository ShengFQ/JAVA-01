package com.geekbang.shengfq.week2.netty.homework;

import com.geekbang.shengfq.week2.MyHttpClient;
import com.geekbang.shengfq.week2.netty.filter.IHttpResponseFilter;
import com.geekbang.shengfq.week2.netty.filter.TokenHttpResponseFilter;
import com.geekbang.shengfq.week2.netty.router.GatewayHttpRandomRouter;
import com.geekbang.shengfq.week2.netty.router.IGatewayHttpRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.apache.http.HttpHeaders.CONNECTION;

/**
 * 输出处理器,处理网关服务器业务逻辑输出
 * 调用代理访问
 * 调用过滤器
 * 调用路由器
 * @author sheng
 * @date 2021-01-26
 * */
public class GatewayServerOutboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServerOutboundHandler.class);

    /**
     * 路由表
     * */
    private List<String> proxyServer;
    private IGatewayHttpRouter router;
    private IHttpResponseFilter responseFilter;
    public GatewayServerOutboundHandler(List<String> proxyServer){
        this.proxyServer=proxyServer;
        this.router=new GatewayHttpRandomRouter();
        this.responseFilter=new TokenHttpResponseFilter("shengfq");
    }

    /**
     * 处理器处理方法业务逻辑
     * */
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx){
        FullHttpResponse response = null;
        try {
            MyHttpClient httpClient=new MyHttpClient();
            String serverUrl=router.route(this.proxyServer);
            logger.info("########### remoter url:{}",serverUrl);
            String getResult=httpClient.doGet(serverUrl);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(getResult.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
            responseFilter.filter(response);

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
}
