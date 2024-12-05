package fr.diginamic.hello.repositories;

import fr.diginamic.hello.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
}
