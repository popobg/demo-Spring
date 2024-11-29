package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VilleServiceTest {
    @Autowired
    private VilleService villeService;

    private static List<Ville> constructVilles() {
        List<Ville> villes = new ArrayList<Ville>();

        Departement ain = new Departement(2, "Ain", "01");
        Departement alpesHauteProvence = new Departement(5, "Alpes-de-Haute-Provence", "04");

        Collections.addAll(villes,
                new Ville(13497, "Bourg-en-Bresse", 41365, ain),
                new Ville(13721, "Oyonnax", 22559, ain),
                new Ville(13738, "Manosque", 21868, alpesHauteProvence),
                new Ville(13904, "Digne-les-Bains", 16186, alpesHauteProvence),
                new Ville(14007, "Oyonnax", 22559, ain),
                new Ville(14089,"Gex",12652, ain),
                new Ville(14134,"Saint-Genis-Pouilly",11892, ain),
                new Ville(14158,"Bellegarde-sur-Valserine",11666, ain));

        return villes;
    }

    @Test
    void getVillesOk() throws RessourceNotFoundException {
        List<Ville> actualVilles = villeService.getVilles();
        assertEquals(constructVilles(), actualVilles);
    }

    @Test
    void getVillesPagination() {
    }

    @Test
    void getVilleById() {
    }

    @Test
    void getVillesByNom() {
    }

    @Test
    void insertVille() {
    }

    @Test
    void updateVille() {
    }

    @Test
    void deleteVille() {
    }

    @Test
    void extractVillesByNomStartingWith() {
    }

    @Test
    void extractVillesByNbHabGreaterThan() {
    }

    @Test
    void extractVillesByDepartementCodeOrderByNbHabDesc() {
    }

    @Test
    void extractVillesByNbHabBetween() {
    }

    @Test
    void extractVillesByDepartementCodeAndNbHabitantsGreaterThan() {
    }

    @Test
    void extractVillesByDepartementCodeAndNbHabBetween() {
    }
}