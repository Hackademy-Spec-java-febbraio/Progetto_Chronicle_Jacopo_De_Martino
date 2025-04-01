package it.aulab.aulabchronicle.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    public UserServiceImplement(UserRepository repo){
        this.repo = repo;
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User newUser = new User();
        newUser.setUsername(userDto.getFristname() + " " + userDto.getLastname());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder().encode(userDto.getPassword()));
        repo.save(newUser);
    }

    @Override
    public User findByEmail(String email) {
        return repo.findByEmailIgnoreCase(email);
    }

    @Override
    public User findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOneByUsername'");
    }

    @Override
    public List<User> findListByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findListByEmail'");
    }

    @Override
    public List<User> findByListUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByListUsername'");
    }
    
}
