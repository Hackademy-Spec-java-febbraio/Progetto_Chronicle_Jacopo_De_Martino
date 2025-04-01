package it.aulab.aulabchronicle.services;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImplement implements UserServiceInterface {

    final private UserRepository repo;
    final private PasswordEncoder encoder;

    @Autowired
    public UserServiceImplement(UserRepository repo, PasswordEncoder encoder){
        this.repo = repo;
        this.encoder = encoder;
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
        repo.save(newUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return repo.findByEmail(email);
    }



}
