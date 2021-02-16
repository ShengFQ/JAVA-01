package io.geekbang.shengfq.springboot;


import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * spring加载类的方式
 * @author sheng
 * @date 2021-02-15
 * */
@Data
@NoArgsConstructor
public class Student {
    private int age;
    private String name;
    public Student(int age,String name){
        this.age=age;
        this.name=name;
    }

    public  void print(){
        System.out.println("student:"+this.toString());
    }
}
