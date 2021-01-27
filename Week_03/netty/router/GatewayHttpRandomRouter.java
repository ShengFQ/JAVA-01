package com.geekbang.shengfq.week2.netty.router;

import java.util.List;
import java.util.Random;

/**
 * 随机码实现路由器
 * */
public class GatewayHttpRandomRouter implements IGatewayHttpRouter {
    /**
     * 路由策略
     * load balance
     * random
     * RoundRibbon
     * Weight
     *
     * @param backendUrls
     */
    @Override
    public String route(List<String> backendUrls) {
        if(backendUrls==null){
            throw new NullPointerException("endpoint urls is Empty");
        }
        int size=backendUrls.size();
        Random random=new Random(System.currentTimeMillis());
        int index=random.nextInt(size);
       return backendUrls.get(index);
    }
}
