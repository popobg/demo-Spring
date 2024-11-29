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
class VilleGetServiceTest {
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
        assertThrows(RequeteIncorrecteException.class, () -> villeService.getVilleById(null));
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
        // Gère tous les appels aux repositories faits dans la méthode insertVille
        Mockito.when(villeRepository.save(new Ville(13566, "Barcelonnette", 2539, departements.get(1)))).thenReturn(null);
        Mockito.when(villeRepository.findByNom("Barcelonnette")).thenReturn(List.of());
        Mockito.when(departementRepository.findByCode("04")).thenReturn(Optional.ofNullable(departements.get(1)));

        villeService.insertVille(new Ville(13566, "Barcelonnette", 2539, departements.get(1)));
    }

    @Test
    void testInsertVilleRessourceExistanteException() {
        Mockito.when(villeRepository.findByNom("Oyonnax")).thenReturn(List.of(villes.get(1)));
        assertThrows(RessourceExistanteException.class, () -> villeService.insertVille(villes.get(1)));
    }

    @Test
    void testInsertVilleRessourceNotFoundException() {
        Mockito.when(departementRepository.findByCode("34")).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.insertVille(new Ville(13455, "Montpellier", 302454, new Departement(3, "Hérault", "34"))));
    }

    @Test
    void testInsertVilleRequeteIncorrecteException() {
        Mockito.when(departementRepository.findByCode("")).thenReturn(Optional.empty());
        assertThrows(RequeteIncorrecteException.class, () -> villeService.insertVille(new Ville(13455, "Montpellier", 302454, new Departement(3, "Hérault", ""))));
    }

    @Test
    void testUpdateVilleOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.get(1)));
        villeService.updateVille(13721L, new Ville(13721L, "Oyonnax", 23000, departements.getFirst()));
    }

    @Test
    void testUpdateVilleRessourceNotFoundException() {
        Mockito.when(villeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.updateVille(3L, new Ville(13721L, "Oyonnax", 23000, departements.getFirst())));
    }

    @Test
    void testUpdateVilleRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.updateVille(null, new Ville(13721L, "Oyonnax", 23000, departements.getFirst())));
    }

    @Test
    void testDeleteVilleOk() throws RequeteIncorrecteException, RessourceNotFoundException {
        Mockito.when(villeRepository.findById(13721L)).thenReturn(Optional.of(villes.get(1)));
        villeService.deleteVille(13721L);
    }

    @Test
    void testDeleteVilleRessourceNotFoundException() {
        Mockito.when(villeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.updateVille(3L, new Ville(13721L, "Oyonnax", 23000, departements.getFirst())));
    }

    @Test
    void testDeleteVilleRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.deleteVille(null));
    }

    @Test
    void testExtractVillesByNomStartingWithOk() throws RessourceNotFoundException {
        Mockito.when(villeRepository.findByNomStartingWith("Oy")).thenReturn(List.of(villes.get(1)));
        assertEquals(List.of(villes.get(1)), villeService.extractVillesByNomStartingWith("Oy"));
    }

    @Test
    void testExtractVillesByNomStartingWithRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNomStartingWith("Oya")).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNomStartingWith("Oya"));
    }

    @Test
    void testExtractVillesByNbHabGreaterThanOk() throws RessourceNotFoundException {
        Mockito.when(villeRepository.findByNbHabitantsGreaterThan(30000)).thenReturn(List.of(villes.getFirst()));
        assertEquals(List.of(villes.getFirst()), villeService.extractVillesByNbHabGreaterThan(30000));
    }

    @Test
    void testExtractVillesByNbHabGreaterThanRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNbHabitantsGreaterThan(50000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNbHabGreaterThan(50000));
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
    void testExtractVillesByDepartementCodeOrderByNbHabDescRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.extractVillesByDepartementCodeOrderByNbHabDesc("06", -1));
    }

    @Test
    void testExtractVillesByNbHabBetweenOk() throws RessourceNotFoundException {
        Mockito.when(villeRepository.findByNbHabitantsBetween(15000, 20000)).thenReturn(List.of(villes.get(3)));
        assertEquals(List.of(villes.get(3)), villeService.extractVillesByNbHabBetween(15000, 20000));
    }

    @Test
    void testExtractVillesByNbHabBetweenRessourceNotFoundException() {
        Mockito.when(villeRepository.findByNbHabitantsBetween(50000, 100000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByNbHabBetween(50000, 100000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThanOK() throws RessourceNotFoundException {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("04", 20000)).thenReturn(List.of(villes.get(2)));
        assertEquals(List.of(villes.get(2)), villeService.extractVillesByDepartementCodeAndNbHabitantsGreaterThan("04", 20000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabitantsGreaterThaRessourceNotFoundException() {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("04", 50000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabitantsGreaterThan("04", 20000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenOk() throws RessourceNotFoundException {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsBetween("04", 20000, 30000)).thenReturn(List.of(villes.get(2)));
        assertEquals(List.of(villes.get(2)), villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 20000, 30000));
    }

    @Test
    void testExtractVillesByDepartementCodeAndNbHabBetweenRessourceNotFoundException() {
        Mockito.when(villeRepository.findByDepartementCodeAndNbHabitantsBetween("04", 2000000, 3000000)).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> villeService.extractVillesByDepartementCodeAndNbHabBetween("04", 2000000, 3000000));
    }
}