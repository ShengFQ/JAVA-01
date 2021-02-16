package io.geekbang.shengfq.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class SpringbootApplication implements CommandLineRunner {

    @Resource
    private School school;
    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);

    }

    public void run(String... args) throws Exception {
        school.print();
    }
}
