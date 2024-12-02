package fr.diginamic.hello.repositories;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Cette classe teste aussi indirectement la classe Service
// car on ne peut pas injecter directement le repository.
// La classe service et sa logique métier sont testées
// plus en détail dans la classe VilleServiceTest.
@SpringBootTest
@ActiveProfiles("test")
class VilleRepoTest {
    @Autowired
    private VilleService villeService;

    // Data
    List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));

    private List<Ville> constructVilles() {
        List<Ville> villes = new ArrayList<>();

        Departement ain = this.departements.getFirst();
        Departement alpesHauteProvence = this.departements.get(1);

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
    void testFindAllVillesOk() throws RessourceNotFoundException {
        List<Ville> actualVilles = villeService.getVilles();
        List<Ville> expectedVilles = constructVilles();
        assertEquals(expectedVilles, actualVilles);
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
    void testGetVilleByIdOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Ville actualVille = villeService.getVilleById(13497L);
        assertEquals(constructVilles().getFirst(), actualVille);
    }

    @Test
    void testGetVillesByNomOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVilles = villeService.getVillesByNom("Oyonnax");
        assertEquals(List.of(constructVilles().get(1)), actualVilles);
    }

    @Test
    void testInsertVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        Ville nouvelleVille = new Ville("Barcelonnette", 2359);
        nouvelleVille.setDepartement(departements.get(1));

        villeService.insertVille(nouvelleVille);

        Ville villeAjoutee = villeService.getVillesByNom("Barcelonnette").getFirst();
        // Print les informations de la ville ajoutée
        System.out.println(villeAjoutee.toString());
        assertEquals("Barcelonnette", villeAjoutee.getNom());

        // Rétablit la base de données dans l'état dans lequel elle était avant l'insertion
        // (pour l'intégrité des autres tests)
        villeService.deleteVille(villeAjoutee.getId());
    }

    @Test
    void testUpdateVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Ville villeAModifier = new Ville("Bourg-en-Bresse", 42000);
        villeAModifier.setDepartement(departements.getFirst());

        villeService.updateVille(13497L, villeAModifier);

        Ville villeModifiee = villeService.getVilleById(13497L);
        assertEquals(42000, villeModifiee.getNbHabitants());

        // Rétablit la base de données dans l'état dans lequel elle était avant la modification
        villeAModifier.setNbHabitants(41365);
        villeService.updateVille(13497L, villeAModifier);
    }

    @Test
    void testDeleteVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        // On ajoute une donnée à supprimer ensuite
        Ville villeAInserer = new Ville("Barcelonnette",2359);
        villeAInserer.setDepartement(departements.getFirst());
        villeService.insertVille(villeAInserer);

        List<Ville> villeInseree = villeService.getVillesByNom("Barcelonnette");
        Long idVilleInseree = villeInseree.getFirst().getId();

        villeService.deleteVille(idVilleInseree);
        // Ville supprimée --> son ID n'existe plus dans la base de données --> lève une exception
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVilleById(idVilleInseree));
    }

    @Test
    void testExtractVillesByNomStartingWithOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVillesEnB = villeService.extractVillesByNomStartingWith("B");
        List<Ville> expectedVillesEnB = constructVilles();
        assertEquals(List.of(expectedVillesEnB.getFirst(), expectedVillesEnB.getLast()), actualVillesEnB);
    }

    @Test
    void testExtractVillesByNbHabGreaterThanOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualGrandesVilles = villeService.extractVillesByNbHabGreaterThan(30000);
        assertEquals(List.of(constructVilles().getFirst()), actualGrandesVilles);
    }

    @Test
    void testExtractVillesByDepartementCodeOrderByNbHabDescOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVilles01 = villeService.extractVillesByDepartementCodeOrderByNbHabDesc("01", 3);
        List<Ville> expectedVilles01 = constructVilles();
        assertEquals(List.of(expectedVilles01.getFirst(), expectedVilles01.get(1), expectedVilles01.get(4)), actualVilles01);
    }

    @Test
    void testExtractVillesByNbHabBetweenOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVilles = villeService.extractVillesByNbHabBetween(20_000, 30_000);
        List<Ville> expectedVilles = constructVilles();
        assertEquals(List.of(expectedVilles.get(1), expectedVilles.get(2)), actualVilles);
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVilles = villeService.extractVillesByDepartementCodeAndNbHabGreaterThan("01", 20_000);
        assertEquals(List.of(new Ville(13497, "Bourg-en-Bresse", 41365, departements.getFirst()), new Ville(13721, "Oyonnax", 22559, departements.getFirst())), actualVilles);
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Ville> actualVilles = villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 15_000, 30_000);
        List<Ville> expectedVilles = constructVilles();
        assertEquals(List.of(expectedVilles.get(2), expectedVilles.get(3)), actualVilles);
    }
}