package io.geekbang.shengfq.springboot;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * bean装载练习
 * */
@Data
@NoArgsConstructor
public class Klass {
    private String name;
    List<Student> students;

    public Klass(String name,List<Student> students){
        this.name=name;
        this.students=students;
    }
    public void print(){
        System.out.println("students:"+this.students.size());
        students.forEach(Student::print);
    }
}
