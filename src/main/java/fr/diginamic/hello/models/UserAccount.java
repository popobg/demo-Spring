package fr.diginamic.hello.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<GrantedAuthority> authorities;

    {
        this.authorities = new ArrayList<>();
    }

    public UserAccount() {
    }

    /**
     * Constructeur
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @param authorities autorisations accordées dans l'application
     */
    public UserAccount(String username, String password, String... authorities) {
        this.username = username;
        this.password = password;
        this.authorities = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    /**
     * Getter
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter
     * @return authorities
     */
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Setter
     * @param authorities authorities
     */
    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
