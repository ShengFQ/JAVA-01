package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;

import java.util.List;

public interface IStudentService {
    void saveStudent(List<Student> students);
}
