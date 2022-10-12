package starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
@EntityScan(basePackages = "ru.yandex.practicum.filmorate.model")
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
