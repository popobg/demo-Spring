package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe mapper DTO <--> entités JPA
 */
@Component
public class VilleMapper {
    @Autowired
    private DepartementMapper departementMapper;

    /**
     * Map une entité JPA Ville en VilleDto.
     * @param ville entité JPA Ville
     * @return objet VilleDto
     */
    public VilleDto toDto(Ville ville) {
        VilleDto villeDto = new VilleDto();
        villeDto.setNom(ville.getNom());
        villeDto.setNbHabitants(ville.getNbHabitants());

        if (ville.getDepartement() != null) {
            villeDto.setCodeDepartement(ville.getDepartement().getCode());
            villeDto.setNomDepartement(ville.getDepartement().getNom());
        }

        return villeDto;
    }

    public Ville toEntity(VilleDto villeDto, Departement departement) {
        Ville ville = new Ville();
        ville.setNom(villeDto.getNom());
        ville.setNbHabitants(villeDto.getNbHabitants());
        ville.setDepartement(departement);
        return ville;
    }
}
