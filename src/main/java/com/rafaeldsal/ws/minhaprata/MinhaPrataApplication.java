package com.rafaeldsal.ws.minhaprata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MinhaPrataApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinhaPrataApplication.class, args);
	}

}
