package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Ville;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    @Query("select v from Ville v order by v.nom")
    List<Ville> findAllOrderByNom(Pageable pageable);

    List<Ville> findByNom(String nom);

    List<Ville> findByNomStartingWith(String prefixe);

    List<Ville> findByNbHabitantsGreaterThan(int minHabitants);

    List<Ville> findByNbHabitantsBetween(int minHabitants, int maxHabitants);

    @Query("select v from Ville v where v.departement.code = :departementCode and v.nbHabitants > :minHabitants")
    List<Ville> findByDepartementCodeAndNbHabitantsGreaterThan(String departementCode, int minHabitants);

    @Query("select v from Ville v where v.departement.code = :departementCode and v.nbHabitants between :minHabitants and :maxHabitants")
    List<Ville> findByDepartementCodeAndNbHabitantsBetween(String departementCode, int minHabitants, int maxHabitants);

    @Query("select v from Ville v where v.departement.code = :departementCode order by v.nbHabitants desc")
    List<Ville> findByDepartementCodeOrderByNbHabitantsDesc(String departementCode, Pageable pageable);
}
