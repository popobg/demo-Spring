package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findUserRolesByName(String name);
}
