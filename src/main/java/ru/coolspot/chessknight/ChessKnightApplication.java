package ru.coolspot.chessknight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ChessKnightApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessKnightApplication.class, args);
	}

}
