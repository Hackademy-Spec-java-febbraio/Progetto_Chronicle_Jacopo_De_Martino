package it.aulab.aulabchronicle.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserServiceInterface {
    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request);
    User findByEmail(String email);
    User findByUsername(String username);
    Optional<User> findOneByUsername(String username);
     List<User> findListByEmail(String email);
     List<User> findByListUsername(String username);
}
