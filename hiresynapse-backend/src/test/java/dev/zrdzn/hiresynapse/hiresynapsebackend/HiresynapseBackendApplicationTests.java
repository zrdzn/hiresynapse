package dev.zrdzn.hiresynapse.hiresynapsebackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class HiresynapseBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
