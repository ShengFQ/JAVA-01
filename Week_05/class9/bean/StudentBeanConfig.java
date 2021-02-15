package com.geekbang.shengfq.week5.spring.bean;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过配置化加载bean
 * @author sheng
 * */
@Configuration
@Data
public class StudentBeanConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 装载一个klass对象
     * */
    @Bean("student02")
    public Student build(){
        return new Student(4,"xiaomin");
    }
}
