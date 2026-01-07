package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"model"})
@ComponentScan(basePackages = {"springboot", "dao", "service", "model"})
public class StableManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StableManagerApplication.class, args);
    }
}
