package fr.diginamic.hello.services;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.repositories.DepartementRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DepartementServiceTest {
    @Autowired
    private DepartementService departementService;

    @MockitoBean
    private DepartementRepository departementRepository;

    // Data
    private final List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));
    @Autowired
    private VilleService villeService;

    @Test
    void testGetDepartementsOk() throws RessourceNotFoundException {
        Mockito.when(departementRepository.findAll()).thenReturn(departements);
        assertEquals(departements, departementService.getDepartements());
    }

    @Test
    void testGetDepartementsRessourceNotFoundException() {
        Mockito.when(departementRepository.findAll()).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> departementService.getDepartements());
    }

    @Test
    void testGetDepartementsPaginationOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(departementRepository.findAllOrderByNom(PageRequest.of(0, 2))).thenReturn(departements);
        assertEquals(departements, departementService.getDepartementsPagination(2));
    }

    @Test
    void testGetDepartementsPaginationRessourceNotFoundException() {
        Mockito.when(departementRepository.findAllOrderByNom(PageRequest.of(0, 2))).thenReturn(List.of());
        assertThrows(RessourceNotFoundException.class, () -> departementService.getDepartementsPagination(2));
    }

    @Test
    void testGetDepartementsPaginationRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> departementService.getDepartementsPagination(0));
    }

    @Test
    void testGetDepartementByIdOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(departementRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));
        assertEquals(departements.getFirst(), departementService.getDepartementById(1L));
    }

    @Test
    void testGetDepartementByIdRessourceNotFoundException() {
        Mockito.when(departementRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> departementService.getDepartementById(3L));
    }

    @Test
    void testGetDepartementByIdRequeteIncorrecteNegatifException() {
        assertThrows(RequeteIncorrecteException.class, () -> departementService.getDepartementById(-1L));
    }

    @Test
    void testGetDepartementByIdRequeteIncorrecteNullException() {
        assertThrows(RequeteIncorrecteException.class, () -> departementService.getDepartementById(null));
    }

    @Test
    void testGetDepartementByCodeOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(departementRepository.findByCode("04")).thenReturn(Optional.of(departements.get(1)));
        assertEquals(departements.get(1), departementService.getDepartementByCode("04"));
    }

    @Test
    void testGetDepartementByCodeRessourceNotFoundException() {
        Mockito.when(departementRepository.findByCode("37")).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> departementService.getDepartementByCode("37"));
    }

    @Test
    void testGetDepartementByCodeRequeteIncorrecteException(){
        assertThrows(RequeteIncorrecteException.class, () -> departementService.getDepartementByCode(""));
    }

    @Test
    void testInsertDepartementOk() throws RessourceExistanteException {
        Mockito.when(departementRepository.save(new Departement("Hérault", "34"))).thenReturn(null);
        Mockito.when(departementRepository.findByCode("34")).thenReturn(Optional.empty());

        departementService.insertDepartement(new Departement("Hérault", "34"));
    }

    @Test
    void testInsertDepartementRessourceExistanteException() {
        Mockito.when(departementRepository.save(new Departement("Ain", "01"))).thenReturn(null);
        Mockito.when(departementRepository.findByCode("01")).thenReturn(Optional.of(departements.getFirst()));

        assertThrows(RessourceExistanteException.class, () -> departementService.insertDepartement(new Departement("Ain", "01")));
    }

    @Test
    void testUpdateDepartementOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(departementRepository.save(departements.getFirst())).thenReturn(null);
        Mockito.when(departementRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));

        departementService.updateDepartement(1L, new Departement("Ain", "02"));
    }

    @Test
    void testUpdateDepartementRessourceNotFoundException() {
        Mockito.when(departementRepository.save(departements.getFirst())).thenReturn(null);
        Mockito.when(departementRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RessourceNotFoundException.class, () -> departementService.updateDepartement(3L, new Departement("Ain", "02")));
    }

    @Test
    void testUpdateDepartementRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> departementService.updateDepartement(null, new Departement("Ain", "02")));
    }

    @Test
    void testDeleteDepartementOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Mockito.when(departementRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));
        departementService.deleteDepartement(1L);
    }

    @Test
    void testDeleteDepartementRessourceNotFoundException() {
        Mockito.when(departementRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RessourceNotFoundException.class, () -> villeService.deleteVille(5L));
    }

    @Test
    void testDeleteDepartementRequeteIncorrecteException() {
        assertThrows(RequeteIncorrecteException.class, () -> villeService.deleteVille(-3L));
    }
}