package it.aulab.aulabchronicle.controllers.view;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.models.CareerRequest;
import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.RoleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;
import it.aulab.aulabchronicle.services.CareerRequestService;

@Controller
@RequestMapping("/operations")
public class OperationViewController {

    final private RoleRepository roleRepository;
    final private UserRepository userRepository;
    final private CareerRequestService careerRequestService;
    public OperationViewController(RoleRepository roleRepository, UserRepository userRepository, CareerRequestService careerRequestService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.careerRequestService = careerRequestService;
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

    @PostMapping("/career/request/save")
public String careerRequestStore(@ModelAttribute("careerRequest")CareerRequest careerRequest,Principal principal , RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());
        if(careerRequestService.isRoleAlredyAssigned(user,careerRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Impossibile inviare la richiesta. Questo utente già ha assegnato un ruolo a questa attività.");
            return "redirect:/";
        };
        careerRequestService.save(careerRequest,user);
        redirectAttributes.addFlashAttribute("messageSuccessRequest", "Richiesta inviata con successo!");

        return "redirect:/";
    } 
        
    
    
}
