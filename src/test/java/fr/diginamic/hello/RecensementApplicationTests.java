package fr.diginamic.hello;

import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.HelloService;
import fr.diginamic.hello.services.VilleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RecensementApplicationTests {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementService departementService;

    @Test
    void contextLoadsVille() {
        assertNotNull(villeService);
    }

    @Test
    void contextLoadsDepartement() {
        assertNotNull(departementService);
    }

}
