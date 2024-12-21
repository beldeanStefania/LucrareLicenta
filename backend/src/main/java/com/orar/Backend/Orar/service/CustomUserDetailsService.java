package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.exception.UserNotFoundException;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Găsește utilizatorul în baza de date pe baza numelui de utilizator
        User user = null;
        try {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Construieste un obiect UserDetails pentru Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())  // Mapează rolul utilizatorului în autoritate
        );
    }

    // Transformă rolul în autoritate pentru Spring Security
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Rol rol) {
        return Collections.singleton(new SimpleGrantedAuthority(rol.getName()));
    }
}

