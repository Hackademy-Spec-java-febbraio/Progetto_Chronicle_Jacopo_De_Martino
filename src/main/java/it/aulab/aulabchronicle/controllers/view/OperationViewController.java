package it.aulab.aulabchronicle.controllers.view;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.aulabchronicle.models.CareerRequest;
import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.repositories.RoleRepository;

@Controller
@RequestMapping("/operations")
public class OperationViewController {

    final private RoleRepository roleRepository;
    public OperationViewController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/career/request")
    public String careerRequestCreate(Model model){
        model.addAttribute("title", "Inserisci la tua richiesta");
        model.addAttribute("careerRequest",new CareerRequest());
        List<Role> roles = roleRepository.findAll();
        //! cicliamo tutti i ruoli tranne ROLE_USER
        roles.removeIf(e-> e.getName().equals("ROLE_USER"));
        model.addAttribute("roles", roles);
        return "career/request-form";
    }
    
}
