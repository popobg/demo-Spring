package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VilleRepoTest {
    @Autowired
    private VilleService villeService;

//    @MockitoBean
//    private VilleRepository villeRepository;

    private static List<Ville> constructVilles() {
        List<Ville> villes = new ArrayList<Ville>();

        Departement ain = new Departement(1, "Ain", "01");
        Departement alpesHauteProvence = new Departement(2, "Alpes-de-Haute-Provence", "04");

        Collections.addAll(villes,
                new Ville(13497, "Bourg-en-Bresse", 41365, ain),
                new Ville(13721, "Oyonnax", 22559, ain),
                new Ville(13738, "Manosque", 21868, alpesHauteProvence),
                new Ville(13904, "Digne-les-Bains", 16186, alpesHauteProvence),
                new Ville(14007, "Ambérieu-en-Bugey", 14081, ain),
                new Ville(14089,"Gex",12652, ain),
                new Ville(14134,"Saint-Genis-Pouilly",11892, ain),
                new Ville(14158,"Bellegarde-sur-Valserine",11666, ain));

        return villes;
    }

    @Test
    void testGetVillesOk() throws RessourceNotFoundException {
        List<Ville> actualVilles = villeService.getVilles();
        assertEquals(constructVilles(), actualVilles);
    }

    @Test
    void testGetVillesRessourceNotFoundException() {
//        Mockito.when(villeRepository.findAll()).thenReturn(List.of());
//        assertThrows(RessourceNotFoundException.class, () -> villeService.getVilles());
    }

    @Test
    void testGetVillesPaginationOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actual5Villes = villeService.getVillesPagination(5);
        // Récupère les 5 premières villes triées par nom
        List<Ville> expected5Villes = constructVilles()
                    .stream()
                    .sorted(Comparator.comparing(Ville::getNom))
                    .limit(5)
                    .toList();

        assertEquals(expected5Villes, actual5Villes);
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