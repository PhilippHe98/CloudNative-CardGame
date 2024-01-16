package de.hskl.cloudnative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudnativeApplication {

	public static void main(String[] args) {
		// http://localhost:8080/swagger-ui/index.html for endpoint documentation
		SpringApplication.run(CloudnativeApplication.class, args);
	}

}
