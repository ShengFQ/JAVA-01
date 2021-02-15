package com.geekbang.shengfq.week5.spring.aop;
/**
 * 目标业务接口
 *
 * */
public interface OrderService {
     void save(Integer orderId,String name);
    void update(Integer orderId,String name);
    String getByName(String name);
}
