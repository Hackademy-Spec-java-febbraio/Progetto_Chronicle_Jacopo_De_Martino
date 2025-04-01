package it.aulab.aulabchronicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AulabchronicleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AulabchronicleApplication.class, args);
	}
	
@Bean
public PasswordEncoder getPasswordEncoder() {
	return new BCryptPasswordEncoder();
}
}
