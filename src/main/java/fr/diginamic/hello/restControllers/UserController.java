package fr.diginamic.hello.restControllers;

import fr.diginamic.hello.dto.AuthRequestDto;
import fr.diginamic.hello.dto.JwtResponseDto;
import fr.diginamic.hello.models.UserInfo;
import fr.diginamic.hello.services.JwtService;
import fr.diginamic.hello.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public UserController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> register(@RequestBody UserInfo user) {
        userDetailsServiceImpl.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/api/login")
    public JwtResponseDto AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));

        if (auth.isAuthenticated()) {
            return JwtResponseDto.builder()
                    .accessToken(jwtService.generateToken(authRequestDto.getUsername())).build();
        }
        else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }


}
