package com.vector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description:
 * @Title: BlogAdminApplication
 * @Package com.vector
 * @Author 芝士汉堡
 * @Date 2022/9/21 17:06
 */
@SpringBootApplication
@MapperScan("com.vector.mapper")
@EnableScheduling
@EnableSwagger2
@SuppressWarnings("all")
public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class,args);
    }
}
