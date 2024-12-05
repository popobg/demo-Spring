package fr.diginamic.hello.mapper;

import fr.diginamic.hello.models.UserAccount;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

// On peut implémenter le mapper dans le model UserAccount
public class UserMapper {
    public static UserDetails toUserDetails(UserAccount user) {
        return User.builder().username(user.getUsername()).password(user.getPassword()).authorities(user.getAuthorities()).build();
    }
}
