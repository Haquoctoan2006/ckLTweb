package vn.edu.ute.cklt_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CkltWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(CkltWebApplication.class, args);
	}

}
