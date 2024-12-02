package fr.diginamic.hello.repositories;

import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.services.DepartementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class DepartementRepoTest {
    @Autowired
    private DepartementService departementService;

    // Data
    List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));

    @Test
    void testGetDepartementsOk() throws RessourceNotFoundException {
        List<Departement> departements = departementService.getDepartements();
        assertEquals(this.departements, departements);
    }

    @Test
    void testGetDepartementsPaginationOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        List<Departement> departements1 = departementService.getDepartementsPagination(1);
        assertEquals(List.of(this.departements.getFirst()), departements1);
    }

    @Test
    void testGetDepartementByIdOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Departement departementAin = departementService.getDepartementById(1L);
        assertEquals(this.departements.getFirst(), departementAin);
    }

    @Test
    void testGetDepartementByCodeOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Departement departementAlpes = departementService.getDepartementByCode("04");
        assertEquals(this.departements.get(1), departementAlpes);
    }

    @Test
    void testInsertDepartementOk() throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        Departement nouveauDepartement = new Departement("Ariège", "09");
        nouveauDepartement.setVilles(new ArrayList<>());

        departementService.insertDepartement(nouveauDepartement);

        Departement departementAjoute = departementService.getDepartementByCode("09");
        System.out.println(departementAjoute);
        assertEquals("Ariège", departementAjoute.getNom());

        // Rétablit la base de données dans l'état dans lequel elle était avant l'insertion
        departementService.deleteDepartement(departementAjoute.getId());
    }

    @Test
    void testUpdateDepartementOk() throws RessourceNotFoundException, RequeteIncorrecteException {
        Departement departementAModifier = new Departement("Ain", "01");
        departementAModifier.setCode("02");

        departementService.updateDepartement(1L, departementAModifier);

        Departement departementModifie = departementService.getDepartementById(1L);
        assertEquals("02", departementModifie.getCode());

        // Rétablit la base de données dans l'état dans lequel elle était avant la modification
        departementAModifier.setCode("01");
        departementService.updateDepartement(1L, departementAModifier);
    }

    @Test
    void testDeleteDepartementOk() throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        // On ajoute une donnée à supprimer ensuite
        Departement nouveauDepartement = new Departement("Ariège", "09");
        nouveauDepartement.setVilles(new ArrayList<>());
        departementService.insertDepartement(nouveauDepartement);

        Departement departementInsere = departementService.getDepartementByCode("09");
        Long idDepartementInsere = departementInsere.getId();

        departementService.deleteDepartement(idDepartementInsere);
        // Departement supprimé --> son ID n'existe plus dans la base de données --> lève une exception
        assertThrows(RessourceNotFoundException.class, () -> departementService.getDepartementById(idDepartementInsere));
    }
}
