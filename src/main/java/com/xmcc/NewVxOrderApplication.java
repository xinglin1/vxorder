package com.xmcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableSwagger2
public class NewVxOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewVxOrderApplication.class, args);
    }

}
