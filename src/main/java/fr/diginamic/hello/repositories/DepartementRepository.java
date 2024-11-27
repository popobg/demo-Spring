package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.Departement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
    @Query("select d from Departement d order by d.nom")
    List<Departement> findAllOrderByNom(Pageable pageable);

    List<Departement> findByNom(String nom);

    List<Departement> findByNomStartingWith(String prefixe);

    Optional<Departement> findByCode(String code);
}
