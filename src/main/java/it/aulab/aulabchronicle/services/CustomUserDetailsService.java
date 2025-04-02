package it.aulab.aulabchronicle.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepository repo;

    @Autowired
    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Trova l'utente dal database
        User user = repo.findByEmail(username);
        
        // Se l'utente non esiste, lancia un'eccezione
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Crea e restituisci l'oggetto CustomUserDetails
        return new CustomUserDetails(
                user.getId(),
                user.getUsername(), 
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
                );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = null;
        if(roles.size() != 0){
            mapRoles = roles.stream()
                   .map(role -> new SimpleGrantedAuthority( role.getName()))
                   .collect(Collectors.toList());
        }else{
            mapRoles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return mapRoles;
    }

    //!Sostituito con mapRolesToAuthorities
    // private Collection<? extends GrantedAuthority> getAuthorities() {
        // Assegna un'autorità di base per l'utente, puoi personalizzarla in base ai ruoli
    //     return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    // }
    
}
