package com.rafaeldsal.minha.prata.minha_prata;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinhaPrataApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("MYSQL_HOST", dotenv.get("MYSQL_HOST"));
		System.setProperty("MYSQL_PORT", dotenv.get("MYSQL_PORT"));
		System.setProperty("MYSQL_USER", dotenv.get("MYSQL_USER"));
		System.setProperty("MYSQL_DATABASE", dotenv.get("MYSQL_DATABASE"));
		System.setProperty("MYSQL_ROOT_PASSWORD", dotenv.get("MYSQL_ROOT_PASSWORD"));
		System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));

		SpringApplication.run(MinhaPrataApplication.class, args);
	}

}
