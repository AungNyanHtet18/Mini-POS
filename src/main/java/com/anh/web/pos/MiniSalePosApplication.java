package com.anh.web.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:/sql.properties")
public class MiniSalePosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniSalePosApplication.class, args);
	}

}
