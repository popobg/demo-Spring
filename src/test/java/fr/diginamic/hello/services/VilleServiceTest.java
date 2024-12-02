package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class VilleServiceTest {
    @Autowired
    private VilleService villeService;

    @MockitoBean
    private VilleRepository villeRepository;

    @MockitoBean
    private DepartementRepository departementRepository;

    // Data
    private final List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));

    private final List<Ville> villes = List.of(new Ville(13497, "Bourg-en-Bresse", 41365, departements.getFirst()),
            new Ville(13721, "Oyonnax", 22559, departements.getFirst()),
            new Ville(13738, "Manosque", 21868, departements.get(1)),
            new Ville(13904, "Digne-les-Bains", 16186, departements.get(1)),
            new Ville(14007, "Ambérieu-en-Bugey", 14081, departements.getFirst()));

    @Test
    void testGetVillesOk() throws RessourceNotFoundException {
        // ARRANGE
        Mockito.when(villeRepository.findAll()).thenReturn(villes);

        // ACT
        List<Ville> actualVilles = villeService.getVilles();

        // ASSERT
        assertEquals(villes, actualVilles);
    }

    @Test
    void testGetVillesRessourceNotFoundException() {
        // Renvoie une liste vide
        Mockito.when(villeRepository.findAll()).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVilles());
    }

    @Test
    void testGetVillesPaginationOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findAllOrderByNom(PageRequest.of(0, 5))).thenReturn(villes);
        List<Ville> actualVilles = villeService.getVillesPagination(5);
        assertEquals(villes, actualVilles);
    }

    @Test
    void testGetVillesPaginationRessourceNotFoundException() {
        // Renvoie une liste vide
        Mockito.when(villeRepository.findAllOrderByNom(PageRequest.of(0, 5))).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVillesPagination(5));
    }

    @Test
    void testGetVillesPaginationRequeteIncorrecteException() {
        Mockito.when(villeRepository.findAllOrderByNom(PageRequest.of(0, 1))).thenReturn(villes);
        assertThrows(RequeteIncorrecteException.class, () -> villeService.getVillesPagination(-1));
    }

    @Test
    void testGetVilleByIdOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.getFirst()));
        Ville actualVille = villeService.getVilleById(13721L);
        assertEquals(villes.getFirst(), actualVille);
    }

    @Test
    void testGetVilleByIdRessourceNotFoundException() {
        Mockito.when(villeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVilleById(3L));
    }

    @Test
    void testGetVilleByIdRequeteIncorrecteException() {
        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.getFirst()));
        assertThrows(RequeteIncorrecteException.class, () -> villeService.getVilleById(-1L));
    }

    @Test
    void tesGetVillesByNomOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByNom("Oyonnax")).thenReturn(List.of(villes.get(1)));
        List<Ville> actualVilles = villeService.getVillesByNom("Oyonnax");
        assertEquals(List.of(villes.get(1)), actualVilles);
    }

    @Test
    void tesGetVillesByNomRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNom("Oyonnax")).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVillesByNom("Auvergne"));
    }

    @Test
    void tesGetVillesByNomRequeteIncorrecteException() {
        Mockito.when(villeRepository.findByNom("")).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.getVillesByNom("Auvergne"));
    }

    @Test
    void testInsertVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        // Nouvelle ville à ajouter
        Ville nouvelleVille = new Ville("Barcelonnette", 2539);
        nouvelleVille.setDepartement(departements.get(1));

        // Gère tous les appels aux repositories faits dans la méthode insertVille
        Mockito.when(villeRepository.save(nouvelleVille)).thenReturn(null);
        Mockito.when(villeRepository.findByNom("Barcelonnette")).thenReturn(List.of());
        Mockito.when(departementRepository.findByCode("04")).thenReturn(Optional.ofNullable(departements.get(1)));

        villeService.insertVille(nouvelleVille);
    }

    @Test
    void testInsertVilleRessourceExistanteException() {
        Mockito.when(villeRepository.findByNom("Oyonnax")).thenReturn(List.of(villes.get(1)));
        assertThrows(RessourceExistanteException.class, () -> villeService.insertVille(villes.get(1)));
    }

    @Test
    void testInsertVilleRessourceNotFoundException() {
        Ville nouvelleVille = new Ville("Barcelonnette", 2539);
        nouvelleVille.setDepartement(new Departement("Hérault", "34"));

        Mockito.when(departementRepository.findByCode("34")).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.insertVille(nouvelleVille));
    }

    @Test
    void testInsertVilleRequeteIncorrecteException() {
        Ville nouvelleVille = new Ville("Barcelonnette", 2539);
        nouvelleVille.setDepartement(new Departement("Hérault", ""));

        Mockito.when(departementRepository.findByCode("")).thenReturn(Optional.empty());
        assertThrows(RequeteIncorrecteException.class, () -> villeService.insertVille(nouvelleVille));
    }

    @Test
    void testUpdateVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Ville ville = new Ville( "Oyonnax", 23000);
        ville.setDepartement(departements.getFirst());

        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.get(1)));
        villeService.updateVille(13721L, ville);
    }

    @Test
    void testUpdateVilleRessourceNotFoundException() {
        Ville ville = new Ville( "Oyonnax", 23000);
        ville.setDepartement(departements.getFirst());

        Mockito.when(villeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.updateVille(3L, ville));
    }

    @Test
    void testUpdateVilleRequeteIncorrecteException() {
        Ville ville = new Ville( "Oyonnax", 23000);
        ville.setDepartement(departements.getFirst());

        assertThrows(RequeteIncorrecteException.class, () -> villeService.updateVille(null, ville));
    }

    @Test
    void testDeleteVilleOk() throws RequeteIncorrecteException, RessourceNotFoundException {
        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.get(1)));
        villeService.deleteVille(13721L);
    }

    @Test
    void testDeleteVilleRessourceNotFoundException() {
        Mockito.when(villeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.deleteVille(3L));
    }

    @Test
    void testDeleteVilleRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.deleteVille(-3L));
    }

    @Test
    void testExtractVillesByNomStartingWithOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByNomStartingWith("Oy")).thenReturn(List.of(villes.get(1)));
        assertEquals(List.of(villes.get(1)), villeService.extractVillesByNomStartingWith("Oy"));
    }

    @Test
    void testExtractVillesByNomStartingWithRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNomStartingWith("Oya")).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNomStartingWith("Oya"));
    }

    @Test
    void testExtractVillesByNomStartingWithRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByNomStartingWith(""));
    }

    @Test
    void testExtractVillesByNbHabGreaterThanOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByNbHabitantsGreaterThan(30000)).thenReturn(List.of(villes.getFirst()));
        assertEquals(List.of(villes.getFirst()), villeService.extractVillesByNbHabGreaterThan(30000));
    }

    @Test
    void testExtractVillesByNbHabGreaterThanRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNbHabitantsGreaterThan(50000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNbHabGreaterThan(50000));
    }

    @Test
    void testExtractVillesByNbHabGreaterThanRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByNbHabGreaterThan(-2000));
    }

    @Test
    void testExtractVillesByDepartementCodeOrderByNbHabDescOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByDepartementCodeOrderByNbHabitantsDesc("04", PageRequest.of(0, 2))).thenReturn(List.of(villes.get(2), villes.get(3)));
        assertEquals(List.of(villes.get(2), villes.get(3)), villeService.extractVillesByDepartementCodeOrderByNbHabDesc("04", 2));
    }

    @Test
    void testExtractVillesByDepartementCodeOrderByNbHabDescRessourceNotFoundException() {
        Mockito.when(villeRepository.findByDepartementCodeOrderByNbHabitantsDesc("06", PageRequest.of(0, 2))).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByDepartementCodeOrderByNbHabDesc("06", 2));
    }

    @Test
    void testExtractVillesByDepartementCodeOrderByNbHabDescRequeteIncorrecteNException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeOrderByNbHabDesc("06", -1));
    }

    @Test
    void testExtractVillesByDepartementCodeOrderByNbHabDescRequeteIncorrecteCodeException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeOrderByNbHabDesc("", -1));
    }

    @Test
    void testExtractVillesByNbHabBetweenOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByNbHabitantsBetween(15000, 20000)).thenReturn(List.of(villes.get(3)));
        assertEquals(List.of(villes.get(3)), villeService.extractVillesByNbHabBetween(15000, 20000));
    }

    @Test
    void testExtractVillesByNbHabBetweenRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNbHabitantsBetween(50000, 100000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNbHabBetween(50000, 100000));
    }

    @Test
    void testExtractVillesByNbHabBetweenRequeteIncorrecteMinSupMaxException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByNbHabBetween(100000, 2));
    }

    @Test
    void testExtractVillesByNbHabBetweenRequeteIncorrecteMinNegException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByNbHabBetween(-32, -3));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanOK() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("04", 20000)).thenReturn(List.of(villes.get(2)));
        assertEquals(List.of(villes.get(2)), villeService.extractVillesByDepartementCodeAndNbHabGreaterThan("04", 20000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanRessourceNotFoundException() {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("04", 50000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabGreaterThan("04", 20000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanRequeteIncorrecteMinHabException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabGreaterThan("04", -47));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanRequeteIncorrecteCodeNullException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabGreaterThan(null, 20000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsBetween("04", 20000, 30000)).thenReturn(List.of(villes.get(2)));
        assertEquals(List.of(villes.get(2)), villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 20000, 30000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenRessourceNotFoundException() {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsBetween("04", 2000000, 3000000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 2000000, 3000000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenRequeteIncorrecteMinSupMaxException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 10000, 2));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenRequeteIncorrecteCodeException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabBetween("", 100, 20000));
    }
}