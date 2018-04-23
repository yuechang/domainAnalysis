package com.yc;

import com.yc.listener.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 *项目启动类
 */
@SpringBootApplication
@EnableRedisHttpSession //启用分布式session共享
public class App
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication springApplication = new SpringApplication(new Class[] { App.class });
        springApplication.addListeners(new ApplicationListener[] { new ApplicationStartup() });
        springApplication.run(args);
    }
}