package it.aulab.aulabchronicle.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserServiceInterface {
    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request);

    User findUserByEmail(String email);

        
    User find(Long id);
        
    

}
