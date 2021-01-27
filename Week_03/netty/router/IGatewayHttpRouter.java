package com.geekbang.shengfq.week2.netty.router;

import java.util.List;

/**
 * 网关的基础路由接口
 * 传入一个地址列表,根据策略返回目标url
 * @author sheng
 * @date 2021-01-26
 * */
public interface IGatewayHttpRouter {
    /**
     * 路由策略
     *  load balance
     *  random
     *  RoundRibbon
     *  Weight
     *
     * */
    String route(List<String> backendUrls);
}
