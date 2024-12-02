package fr.diginamic.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecensementApplication {

    public static void main(String[] args) {
        // Lance le serveur web intégré à Spring Web
        // (Tomcat, port 8080 par défaut --> changé à 8087 dans les configs)
        SpringApplication.run(RecensementApplication.class, args);
    }
}
