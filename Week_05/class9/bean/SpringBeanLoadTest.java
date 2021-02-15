package com.geekbang.shengfq.week5.spring.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring bean 装载
 * @author sheng
 * @date 2021-02-08
 * */
public class SpringBeanLoadTest {
    public static void main(String[] args) {
        //xml方式 加载bean实例
        ApplicationContext contex=new ClassPathXmlApplicationContext("applicationContext.xml");
        method1(contex);
       // annotation方式
        ApplicationContext context=new AnnotationConfigApplicationContext("com.geekbang.shengfq.week5.spring.bean");
       // method2(context);
        //beanFactory方式
        method3();
    }
    /**
     * Xml方式加载bean
     * */
    private static void method1(ApplicationContext context){
        Student george=(Student) context.getBean("george");
        System.out.println(george.toString());
    }
    /**
     * annotation方式加载bean
     * */
    private static void method2(ApplicationContext context){
        Student student02=(Student) context.getBean("student02");
        student02.print();

        Klass klass02=(Klass)context.getBean("klass02");
        klass02.print();
    }
    /**
     * beanFactory方式加载bean
     * */
    private static void method3(){
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        ConstructorArgumentValues cargs=new ConstructorArgumentValues();
        MutablePropertyValues propertyValues=new MutablePropertyValues();
        propertyValues.addPropertyValue("age",1);
        propertyValues.addPropertyValue("name","kimmking");
        AbstractBeanDefinition bean03=new RootBeanDefinition(Student.class,cargs,propertyValues);
        beanFactory.registerBeanDefinition("student03",bean03);
        Student student03=(Student)beanFactory.getBean("student03");
        student03.print();
    }
}
