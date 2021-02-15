package com.geekbang.shengfq.week5.spring.bean;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 通过配置化加载bean
 * @author sheng
 * */
@Configuration
@Data
public class KlassBeanConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 装载一个klass对象
     * */
    @Bean("klass02")
    public Klass buildKlass(){
        List<Student> students=new ArrayList();
        Optional<Object> student = Optional.ofNullable(applicationContext.getBean("student02"));
        student.ifPresent(obj -> students.add((Student) obj));
        return new Klass("klass02",students);
    }
}
