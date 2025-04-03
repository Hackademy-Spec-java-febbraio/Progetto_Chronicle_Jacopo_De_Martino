package it.aulab.aulabchronicle.services;

import java.util.List;

import org.springframework.stereotype.Service;

import it.aulab.aulabchronicle.models.CareerRequest;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.CareerRequestRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    final private CareerRequestRepository careerRequestRepository; 
    final private EmailService emailService;

    public CareerRequestServiceImpl(CareerRequestRepository careerRequestRepository,EmailService emailService) {
        this.careerRequestRepository = careerRequestRepository;
        this.emailService = emailService;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'careerAccept'");
    }

    @Override
    public CareerRequest find(Long id) {
        return careerRequestRepository.findById(id).get();
    }

}
