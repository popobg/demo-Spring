package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.models.Departement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartementMapper {

    /**
     * Map un objet DepartementDto à partir d'une entité JPA Departement.
     * @param departement entité JPA département
     * @return objet DepartementDto
     */
    public static DepartementDto toDto(Departement departement) {
        return new DepartementDto(departement.getId(), departement.getCode(), departement.getNom(), departement.getVilles().stream().mapToInt(v -> v.getNbHabitants()).sum());
    }

    /**
     * Map une liste d'objets DepartementDto à partir d'une liste d'entités JPA Departement.
     * @param departements liste d'entités Departement
     * @return liste d'objets DepartementDto
     */
    public static List<DepartementDto> toDtos(List<Departement> departements) {
        List<DepartementDto> departementDtos = new ArrayList<>();

        for (Departement departement : departements) {
            departementDtos.add(toDto(departement));
        }

        return departementDtos;
    }

    /**
     * Map une entité JPA Departement à partir d'un objet DepartementDto.
     * @param departementDto objet DepartementDto
     * @return entité JPA Departement
     */
    public static Departement toEntity(DepartementDto departementDto) {
        return new Departement(departementDto.getId(), departementDto.getNomDepartement(), departementDto.getCodeDepartement());
    }
}
