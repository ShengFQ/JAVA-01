package com.geekbang.shengfq.week2.netty.homework;

import java.util.Arrays;
import java.util.List;

/**
 * 网关启动类
 * */
public class GatewayApplication {

    public static void main(String[] args) {
        int port=8888;
        List<String> proxy= Arrays.asList("http://localhost:8801","http://localhost:8802");
        GatewayServer server=new GatewayServer(port,proxy);
        try {
            server.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
