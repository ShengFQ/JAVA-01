package com.geekbang.shengfq.week5.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * aop的原理实现-动态代理
 * 切面,切入点,通知,拦截器
 * @author sheng
 * @date 2021-02-07
 * */
public class MyJDKProxy implements InvocationHandler {
    /**
     * 代理工厂模式
     * 1.创建代理对象
     * 2.为目标方法创建代理拦截
     * */

    private Object targetObject;

    public Object newProxyInstance(Object targetObject){
        this.targetObject=targetObject;
        Object proxyObject=Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader(),targetObject.getClass().getInterfaces(),this);
        return proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //this.targetObject=proxy;
        System.out.println("handler before ... ");
        Object value=method.invoke(this.targetObject,args);
        System.out.println("handler after ... ");
        return value;
    }
}
