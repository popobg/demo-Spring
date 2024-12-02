package fr.diginamic.hello.utils;

import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CSVGenerator {
    private static final String CSV_HEADER_VILLE = "nom ville, nombre d'habitants, code département, nom du département\n";
    private static final String CSV_HEADER_DEPT = "code département, nom du département\n";

    public String generateCSVVille(List<Ville> villes) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER_VILLE);

        for (Ville ville : villes) {
            csvContent.append(ville.getNom()).append(',')
                    .append(ville.getNbHabitants()).append(',')
                    .append(ville.getDepartement().getCode()).append(',')
                    .append(ville.getDepartement().getNom()).append("\n");
        }

        return csvContent.toString();
    }

    public String generateCSVDepartement(List<Departement> departements) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER_DEPT);

        for (Departement dept : departements) {
            csvContent.append(dept.getCode()).append(',')
                    .append(dept.getNom()).append('\n');
        }

        return csvContent.toString();
    }
}
