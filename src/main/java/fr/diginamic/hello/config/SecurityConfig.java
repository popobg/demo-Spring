package fr.diginamic.hello.config;

import fr.diginamic.hello.mapper.UserMapper;
import fr.diginamic.hello.repositories.UserAccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> UserMapper.toUserDetails(userAccountRepository.findByUsername(username));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Autre façon de créer un UserDetailsService avec les données en clair
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//
//        // 1er utilisateur créé
//        // par convention, un mot en majuscule définit un rôle;
//        // roles() est une méthode multi-paramètres.
//        userDetailsManager.createUser(User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER", "OTHER")
//                .build());
//
//        // 2ème utilisateur créé : administrateur système
//        userDetailsManager.createUser(User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build());
//
//        return userDetailsManager;
//    }
}
