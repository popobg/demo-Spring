package fr.diginamic.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        // Lance le serveur web intégré à Spring Web
        SpringApplication.run(HelloApplication.class, args);
    }
}
