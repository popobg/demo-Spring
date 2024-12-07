package fr.diginamic.hello.services;

import fr.diginamic.hello.models.CustomUserDetails;
import fr.diginamic.hello.models.UserInfo;
import fr.diginamic.hello.models.UserRole;
import fr.diginamic.hello.repositories.RoleRepository;
import fr.diginamic.hello.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    @Autowired
    private RoleRepository roleRepository;

//    @PostConstruct
//    public void init() {
//        UserRole userRole = new UserRole();
//        userRole.setName("ROLE_USER");
//        UserRole adminRole = new UserRole();
//        adminRole.setName("ROLE_ADMIN");
//        roleRepository.save(userRole);
//        roleRepository.save(adminRole);
//
//        UserInfo user = new UserInfo();
//        user.setUsername("user");
//        user.setPassword("password");
//        user.setRoles(Set.of(userRole));
//        userRepository.save(user);
//
//        UserInfo admin = new UserInfo();
//        admin.setUsername("admin");
//        user.setPassword("admin");
//        user.setRoles(Set.of(adminRole));
//        userRepository.save(admin);
//    }

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
}
