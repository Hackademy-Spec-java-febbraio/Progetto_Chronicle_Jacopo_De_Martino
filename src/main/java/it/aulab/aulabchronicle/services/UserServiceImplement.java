package it.aulab.aulabchronicle.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.RoleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImplement implements UserServiceInterface {

    final private UserRepository userRepository;
    final private PasswordEncoder encoder;
    final private RoleRepository roleRepository;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, PasswordEncoder encoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

    @Override
public void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    User newUser = new User();
    newUser.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
    newUser.setEmail(userDto.getEmail());
    newUser.setPassword(encoder.encode(userDto.getPassword()));

    // Verifica che il ruolo esista nel database
    Role role = roleRepository.findByName("ROLE_USER");
    newUser.setRoles(List.of(role));
    userRepository.save(newUser);
}

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
