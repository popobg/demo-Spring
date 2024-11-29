package fr.diginamic.hello;

import fr.diginamic.hello.services.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloApplicationTests {

    @Autowired
    private HelloService helloService;

    @Test
    void contextLoads() {
        assertNotNull(helloService);
    }

}
