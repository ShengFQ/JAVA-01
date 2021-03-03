package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * JDBC 原生支持 编程式事务
 * @author shengfq
 * @date 2021年02月16日
 * */
@Slf4j
@Repository
public class StudentJdbcRepository implements StudentDAO {
    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private static final String SQL_BATCH_INSERT_STUDENT = "insert into Student (name, age) values ";
    private static final String SQL_INSERT_STUDENT = "insert into Student (name, age) values (?, ?)";
    private static final String SQL_SELECT_STUDENT = "select * from Student where id = ?";
    private static final String SQL_SELECT_ALL_STUDENT  = "select * from Student";
    private static final String SQL_DELETE_STUDENT = "delete from Student where id = ?";
    private static final String SQL_UPDATE_STUDENT = "update Student set age = ? where id = ?";
    /**
     * This is the method to be used to initialize
     * database resources ie. connection.
     *
     * @param ds
     */
    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource=ds;
       // this.platformTransactionManager=new DataSourceTransactionManager(ds);
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
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(SQL_INSERT_STUDENT);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

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
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Student student=null;
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(SQL_SELECT_STUDENT);
            preparedStatement.setInt(1,id);
            resultSet= preparedStatement.executeQuery();
            student= mapRow(resultSet);
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return student;
    }

    /**
     * This is the method to be used to list down
     * all the records from the Student table.
     */
    @Override
    public List<Student> listStudents() {
        List<Student> students=new ArrayList<>();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Student student=null;
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(SQL_SELECT_ALL_STUDENT);
            resultSet= preparedStatement.executeQuery();
            if(resultSet.next()) {
                student = mapRow(resultSet);
                students.add(student);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
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
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(SQL_DELETE_STUDENT);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

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
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(SQL_UPDATE_STUDENT);
            preparedStatement.setInt(1,age);
            preparedStatement.setInt(2,id);
            preparedStatement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * this is the method to be used to create
     * more record in the Student table
     * 使用的是原始的JDBC方式,连接不停的重建
     * @param students
     */
    @Override
    public void createMore(List<Student> students) throws Exception{
        TransactionDefinition transactionDefinition=new DefaultTransactionDefinition();
        TransactionStatus transactionStatus= this.transactionManager.getTransaction(transactionDefinition);
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        StringBuffer suffix=new StringBuffer();
        try {
            connection=dataSource.getConnection();
            preparedStatement=connection.prepareStatement(" ");

            for(Student student:students){
                suffix.append("(").append("'").append(student.getName()).append("'").append(",").append(student.getAge()).append("),");
            }
            StringBuffer sql=new StringBuffer(SQL_BATCH_INSERT_STUDENT);
            sql.append(suffix.substring(0,suffix.length()-1));
            //log.info("sql:{}",sql.toString());
            preparedStatement.addBatch(sql.toString());
            preparedStatement.executeBatch();
            transactionManager.commit(transactionStatus);

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
                transactionManager.rollback(transactionStatus);
            }
        }
    }


    public Student mapRow(ResultSet rs) throws SQLException {
        Student student = new Student();
        if(rs.first()) {
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setAge(rs.getInt("age"));
        }
        return student;
    }
}
