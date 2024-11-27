package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.models.Departement;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDto toDto(Departement departement) {
        DepartementDto departementDto = new DepartementDto();
        departementDto.setCodeDepartement(departement.getCode());
        departementDto.setNomDepartement(departement.getNom());
        departementDto.setNbHabitants(departement.getVilles().stream().mapToInt(v -> v.getNbHabitants()).sum());
        return departementDto;
    }

    public Departement toEntity(DepartementDto departementDto) {
        Departement departement = new Departement();
        departement.setCode(departementDto.getCodeDepartement());
        departement.setNom(departementDto.getNomDepartement());
        return departement;
    }
}
