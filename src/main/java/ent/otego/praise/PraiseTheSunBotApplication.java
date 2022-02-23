package ent.otego.praise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PraiseTheSunBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PraiseTheSunBotApplication.class, args);
	}

}
