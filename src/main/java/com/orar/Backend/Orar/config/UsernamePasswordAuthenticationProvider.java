package com.orar.Backend.Orar.config;

import com.orar.Backend.Orar.repository.UserRepository;
import com.orar.Backend.Orar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsernamePasswordAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println("Utilizator găsit: " + user.getUsername()); // Log pentru utilizator găsit

            if (passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("Parola este corectă"); // Log pentru parolă corectă
                return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
            } else {
                System.out.println("Parola este incorectă"); // Log pentru parolă incorectă
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            System.out.println("Utilizatorul nu există"); // Log pentru utilizator inexistent
            throw new BadCredentialsException("No user registered with username: " + username);
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
