package com.geekbang.shengfq.week5.spring.aop;
/**
 * 目标接口实现类
 * */
public class OrderServiceImpl  implements OrderService{
    @Override
    public void save(Integer orderId, String name) {
        System.out.println("save order");
    }

    @Override
    public void update(Integer orderId, String name) {
        System.out.println("update order");
    }

    @Override
    public String getByName(String name) {
        return "order";
    }
}
