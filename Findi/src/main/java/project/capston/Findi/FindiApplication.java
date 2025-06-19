package project.capston.Findi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

@SpringBootApplication
public class FindiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindiApplication.class, args);
	}
}
