package fr.diginamic.hello.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implémentation customisée de l'interface UserDetails, permettant de représenter les infos
 * de l'utilisateur et ses rôles pour l'authentification et les autorisations d'accès aux données.
 */
public class CustomUserDetails extends UserInfo implements UserDetails {
    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructeur
     * @param byUsername objet Utilisateur retrouvé en base de données à partir du username
     */
    public CustomUserDetails(UserInfo byUsername) {
        this.username = byUsername.getUsername();
        this.password = byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for (UserRole role : byUsername.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }

        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Getter
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Getter
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
