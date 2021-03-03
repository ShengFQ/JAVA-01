package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
/**
 * spring jdbc 实现dao
 * @author sheng
 * */
@Slf4j
public class StudentJdbcTemplate implements StudentDAO {
    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    /**
     * 1.实例化jdbcTemplateObject
     * */
    private JdbcTemplate jdbcTemplateObject;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
      //  this.platformTransactionManager=new DataSourceTransactionManager(dataSource);
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
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
        //System.out.println("Created Record Name = " + name + " Age = " + age);
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
        //System.out.println("Deleted Record with ID = " + id );
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
        //System.out.println("Updated Record with ID = " + id );
    }

    /**
     * this is the method to be used to create
     * more record in the Student table
     * 使用spring的代理连接池
     * @param students
     */
    @Override
    public void createMore(final List<Student> students) throws Exception {
        try {
            //log.info("batch update sql size:{}",students.size());
            batchUpdate(students);
        }catch (Exception e){
            e.printStackTrace();
          //  transactionManager.rollback(transactionStatus);
        }
    }

    /**
     * 批量提交
     * */
    private void batchUpdate(final List<Student> batch){
        BatchPreparedStatementSetter setter=new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Student student=batch.get(i);
                try{
                    ps.setString(1,student.getName());
                    ps.setInt(2,student.getAge());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        };
        String SQL = "insert into Student (name, age) values (?, ?)";
        jdbcTemplateObject.batchUpdate(SQL,setter);
    }
}
