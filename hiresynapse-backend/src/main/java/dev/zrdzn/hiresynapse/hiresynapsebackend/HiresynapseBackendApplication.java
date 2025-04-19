package dev.zrdzn.hiresynapse.hiresynapsebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class HiresynapseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiresynapseBackendApplication.class, args);
    }

}
