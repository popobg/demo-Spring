package fr.diginamic.hello.mappers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe mapper DTO <--> entités JPA
 */
@Component
public class VilleMapper {

    /**
     * Map une entité JPA Ville en VilleDto.
     * @param ville entité JPA Ville
     * @return objet VilleDto
     */
    public static VilleDto toDto(Ville ville) {
        return new VilleDto(ville.getId(), ville.getNom(), ville.getNbHabitants(), ville.getDepartement().getCode(), ville.getDepartement().getNom());
    }

    /**
     * Map une liste d'objets VilleDto à partir d'une liste d'entités JPA Ville.
     * @param villes liste d'entités Ville
     * @return liste d'objets VilleDto
     */
    public static List<VilleDto> toDtos(List<Ville> villes) {
        List<VilleDto> villeDtos = new ArrayList<>();

        for (Ville ville : villes) {
            villeDtos.add(toDto(ville));
        }

        return villeDtos;
    }

    /**
     * Map une VilleDto en entité JPA
     * @param villeDto objet VilleDto
     * @return objet entité JPA ville
     */
    public static Ville toEntity(VilleDto villeDto) {
        Ville ville = new Ville(villeDto.getId(), villeDto.getNom(), villeDto.getNbHabitants());
        ville.setDepartement(new Departement(villeDto.getNomDepartement(), villeDto.getCodeDepartement()));
        return ville;
    }
}
