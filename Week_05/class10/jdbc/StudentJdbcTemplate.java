package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
/**
 * spring jdbc 实现dao
 * @author sheng
 * */
public class StudentJdbcTemplate implements StudentDAO {
    private DataSource dataSource;
    /**
     * 1.实例化jdbcTemplateObject
     * */
    private JdbcTemplate jdbcTemplateObject;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    /**
     * This is the method to be used to create
     * a record in the Student table.
     *
     * @param name
     * @param age
     */
    @Override
    public void create(String name, Integer age) {
        String SQL = "insert into Student (name, age) values (?, ?)";
        jdbcTemplateObject.update( SQL, name, age);
        System.out.println("Created Record Name = " + name + " Age = " + age);
        return;
    }

    /**
     * This is the method to be used to list down
     * a record from the Student table corresponding
     * to a passed student id.
     *
     * @param id
     */
    @Override
    public Student getStudent(Integer id) {
        String SQL = "select * from Student where id = ?";
        Student student = jdbcTemplateObject.queryForObject(SQL,
                new Object[]{id}, new StudentMapper());
        return student;
    }

    /**
     * This is the method to be used to list down
     * all the records from the Student table.
     */
    @Override
    public List<Student> listStudents() {
        String SQL = "select * from Student";
        List <Student> students = jdbcTemplateObject.query(SQL,
                new StudentMapper());
        return students;
    }

    /**
     * This is the method to be used to delete
     * a record from the Student table corresponding
     * to a passed student id.
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        String SQL = "delete from Student where id = ?";
        jdbcTemplateObject.update(SQL, id);
        System.out.println("Deleted Record with ID = " + id );
        return;
    }

    /**
     * This is the method to be used to update
     * a record into the Student table.
     *
     * @param id
     * @param age
     */
    @Override
    public void update(Integer id, Integer age) {
        String SQL = "update Student set age = ? where id = ?";
        jdbcTemplateObject.update(SQL, age, id);
        System.out.println("Updated Record with ID = " + id );
        return;
    }
}
