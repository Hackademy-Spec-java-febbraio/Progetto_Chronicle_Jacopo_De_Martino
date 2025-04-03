package it.aulab.aulabchronicle.services;



import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.CategoryDto;
import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.RoleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImplement implements UserServiceInterface {

    final private UserRepository userRepository;
    final private PasswordEncoder encoder;
    final private RoleRepository roleRepository;
    final private CustomUserDetailsService detailsService;
    final private AuthenticationManager authenticationManager;
    

    @Autowired
    public UserServiceImplement(UserRepository userRepository, PasswordEncoder encoder,RoleRepository roleRepository, CustomUserDetailsService detailsService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.detailsService = detailsService;
        this.authenticationManager = authenticationManager;
        
    }

    //!Spostata in app
    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

    @Override
    public void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    User user = new User();
    user.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
    user.setEmail(userDto.getEmail());
    user.setPassword(encoder.encode(userDto.getPassword()));

    
    Role role = roleRepository.findByName("ROLE_USER");
    user.setRoles(List.of(role));
    userRepository.save(user);

    authenticateUserAndSetSession(user,userDto,request);
}

     private void authenticateUserAndSetSession(User user, UserDto userDto, HttpServletRequest request) {
         try {
             CustomUserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
             UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDto.getPassword());
             Authentication authentication = authenticationManager.authenticate(authToken);
             SecurityContextHolder.getContext().setAuthentication(authentication);
             HttpSession session = request.getSession(true);
             session.setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());
         } catch (AuthenticationException e) {
             e.printStackTrace();
         }
     }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

       @Override
    public User find(Long id) {
        return userRepository.findById(id).get();
    }

    



}
