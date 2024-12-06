package fr.diginamic.hello.config;

import fr.diginamic.hello.mappers.UserMapper;
import fr.diginamic.hello.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    // On utilise le repo et pas le service car celui-ci utilise passwordEncoder
    // ==> dépendances circulaires à éviter
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> UserMapper.toUserDetails(userAccountRepository.findByUsername(username));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // qu'est-ce-qui est autorisé
        // authenticated = authentifié --> personne qui s'est loggée
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/", "login").permitAll()
                .requestMatchers("/logout").authenticated()
                .requestMatchers("/villes/liste").authenticated()
                // .requestMatchers("/delete/**").hasRole("ADMIN")     // ou "hasAuthority("ROLE_ADMIN")"
                .anyRequest().permitAll())  // denyAll() existe aussi
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
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
