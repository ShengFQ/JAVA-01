package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 业务访问类
 * */
@Service("studentServiceImpl")
public class StudentServiceImpl implements IStudentService {

    StudentJdbcTemplate studentJdbcTemplate;

    public void setStudentJdbcTemplate(StudentJdbcTemplate studentJdbcTemplate) {
        this.studentJdbcTemplate = studentJdbcTemplate;
    }

    @Override
    public void saveStudent(List<Student> students) {
        try {
            studentJdbcTemplate.createMore(students);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
