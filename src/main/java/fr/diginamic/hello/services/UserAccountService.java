package fr.diginamic.hello.services;

import fr.diginamic.hello.models.UserAccount;
import fr.diginamic.hello.repositories.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        System.out.println("Passage dans le init de UserAccountService");
        insertUser(new UserAccount("user", "password", "ROLE_USER"));
        insertUser(new UserAccount("admin", "admin", "ROLE_ADMIN"));
    }

    public List<UserAccount> getUsers() {
        return userAccountRepository.findAll();
    }

    public UserAccount getUserByName(String name) {
        return userAccountRepository.findByUsername(name);
    }

    public UserAccount getUserById(long id) {
        Optional<UserAccount> optUser = userAccountRepository.findById(id);

        if (optUser.isPresent()) {
            return optUser.get();
        }
        else {
            return null;
        }
    }

    public void insertUser(UserAccount userAccount) {
        if (getUserByName(userAccount.getUsername()) == null) {
            System.out.println("save en base de données");
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            userAccountRepository.save(userAccount);
        }
    }

    public void updateUser(long id, UserAccount userAccount) {
        UserAccount oldUser = getUserById(id);

        if (oldUser != null) {
            oldUser.setUsername(userAccount.getUsername());
            oldUser.setPassword(userAccount.getPassword());
            oldUser.setAuthorities(userAccount.getAuthorities());
            userAccountRepository.save(oldUser);
        }
    }

    public void deleteUser(long id) {
        if (userAccountRepository.existsById(id)) {
            userAccountRepository.deleteById(id);
        }
    }
}
