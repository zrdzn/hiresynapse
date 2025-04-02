package dev.zrdzn.hiresynapse.hiresynapsebackend;

import org.springframework.boot.SpringApplication;

public class TestHiresynapseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(HiresynapseBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
