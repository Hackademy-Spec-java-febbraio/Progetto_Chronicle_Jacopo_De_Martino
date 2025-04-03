package it.aulab.aulabchronicle.services;

import java.util.List;

import org.springframework.stereotype.Service;

import it.aulab.aulabchronicle.models.CareerRequest;
import it.aulab.aulabchronicle.models.Role;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.CareerRequestRepository;
import it.aulab.aulabchronicle.repositories.RoleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    final private CareerRequestRepository careerRequestRepository; 
    final private EmailService emailService;
    final private RoleRepository roleRepository;
    final private UserRepository userRepository;

    public CareerRequestServiceImpl(CareerRequestRepository careerRequestRepository,EmailService emailService, RoleRepository roleRepository, UserRepository userRepository) {
        this.careerRequestRepository = careerRequestRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isRoleAlredyAssigned(User user, CareerRequest careerRequest) {
        List<Long> allUserIds = careerRequestRepository.findAllUserIds();

        if(!allUserIds.contains(user.getId())){
            return false;
        }
        List<Long> requests = careerRequestRepository.findByUserId(user.getId());

        return requests.stream().anyMatch(roleId->roleId.equals(careerRequest.getRole().getId()));
    }

    @Override
    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequestRepository.save(careerRequest);

        emailService.sendSimpleEmail("admin@admin.com","Richiesta per ruolo: " + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di " + user.getUsername() + " " + careerRequest.getBody());
    }

    @Override
    public void careerAccept(Long requestId) {
        // recupero della richiesta
        CareerRequest request = careerRequestRepository.findById(requestId).get();
        //dalla richiesta prendo l'utente e il ruolo richiesto
        User user = request.getUser();
        Role role = request.getRole();
        //recupero i ruoli gia in possesso dell'utente
        List<Role> rolesUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        rolesUser.add(newRole);
        //salviamo le modifiche
        user.setRoles(rolesUser);
        userRepository.save(user);
        //setto la richiesta come confermata
        request.setIsChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(user.getEmail(),"Ruolo abilitato", "Ciao la tua richiesta di collaborazione è stata accettata dalla nostra amministazione ecco i tuoi ruoli" + user.getRoles());
    }

    @Override
    public CareerRequest find(Long id) {
        return careerRequestRepository.findById(id).get();
    }

}
