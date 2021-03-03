package com.geekbang.shengfq.week5.spring.jdbc;

import com.geekbang.shengfq.week5.spring.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * class14作业:按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
 * @author sheng
 * @date 2021-03-03
 * */
@Slf4j
public class MultBatchInsertTask {
    public static void main(String[] args) throws Exception {
        method3();
        System.out.println("main is over");
    }

    /**
     * 目标:将100万条记录插入,耗时在10秒内.
     * 通过
     * 1.sql语句改成批量操作 jdbcTemplateObject.batchUpdate();
     * 2.修改mysql执行的sql语句的长度
     SET @@global.max_allowed_packet=8388608;
     SET @@global.bulk_insert_buffer_size=125829120;
     SET @@global.net_buffer_length = 8192;
     jdbc-sql语句拼接为批量插入.

     结果:
     100万 5.8秒
     1000万 50秒
     */
    private static void method3() throws Exception {
        ApplicationContext context;
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IStudentService studentService=(IStudentService)context.getBean("studentServiceImpl");
        List<Student> students=new ArrayList<>();
        for (int i=0;i<1000000;i++){
            Student student=new  Student(i,"auto"+i);
            students.add(student);
        }
        List<List<Student>> partStudents=splitList(students,10000);
        long startTime = System.nanoTime();
        log.info("起始时间:{}",startTime);
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        Future<Integer> result = pool1.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                for (List<Student> part:partStudents ) {
                    studentService.saveStudent(part);
                }
                return students.size();
            }
        });
        log.info("插入记录数:{}" ,result.get());
        long endTime = System.nanoTime();
        log.info("结束时间:{}",endTime);
        long millis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        log.info("总耗时:{}毫秒",millis);
    }

    /**
     * 拆分list为小块
     * **/
    private static List<List<Student>> splitList(List<Student> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        //int num =length % groupSize == 0 ? length / groupSize : length / groupSize +1;
        //or
        int num = ( length + groupSize - 1 )/groupSize ;
        List<List<Student>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }
}