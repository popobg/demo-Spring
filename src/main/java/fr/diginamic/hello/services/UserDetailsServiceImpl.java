package fr.diginamic.hello.services;

import fr.diginamic.hello.models.CustomUserDetails;
import fr.diginamic.hello.models.UserInfo;
import fr.diginamic.hello.models.UserRole;
import fr.diginamic.hello.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service implémentant l'interface de Spring Boot Security.
 * Cette classe permet de créer un objet UserDetails utile pour l'authentification.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /** Repository permettant la communication à la base de données */
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        saveUser(new UserInfo(0, "user", "password", Set.of(new UserRole(0, "USER"))));
        saveUser(new UserInfo(0, "admin", "admin", Set.of(new UserRole(0, "ADMIN"))));
    }

    /**
     * Implémentation de la méthode loadUserByUsername de l'interface UserDetailsService.
     * Utilisée par Spring Security pour retrouver les détails concernant le user pour
     * l'authentification.
     * @param username nom d'un utilisateur, entré par le client au moment du log in
     * @return un objet UserDetails
     * @throws UsernameNotFoundException exception levée si le username est inconnu de
     *                                  la base de données
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return new CustomUserDetails(user);
    }

    public void saveUser(UserInfo user) {
        userRepository.save(user);
    }
}
