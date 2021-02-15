package com.geekbang.shengfq.week5.spring.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib实现动态代理
 * @author sheng
 * @date 2021-02-08
 * */
public class MyCGLibProxy implements MethodInterceptor {
    private Object targetObject;
    /**
     * 创建代理对象
     * */

    public Object createProxyObject(Object targetObject){
        this.targetObject=targetObject;
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(targetObject.getClass());
        enhancer.setCallback(this);
        Object proxyObj=enhancer.create();
        return proxyObj;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object obj=null;
        System.out.println("before intercept...");
        obj=method.invoke(targetObject,objects);
        System.out.println("after intercept...");
        return obj;
    }
}
