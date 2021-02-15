package com.geekbang.shengfq.week5.spring.aop;
/**
 * 动态代理测试类
 * @author sheng
 * @date 2021-02-08
 * */
public class ProxyTest {
    public static void main(String[] args) {
        MyJDKProxy proxy=new MyJDKProxy();
        OrderService orderService=(OrderService) proxy.newProxyInstance(new OrderServiceImpl());
        orderService.save(1,"aa");

        MyCGLibProxy myCGLibProxy=new MyCGLibProxy();
        OrderServiceImpl orderService1=(OrderServiceImpl) myCGLibProxy.createProxyObject(new OrderServiceImpl());
        orderService1.update(1,"bb");
    }
}
