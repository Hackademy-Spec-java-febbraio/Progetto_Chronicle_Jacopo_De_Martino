package it.aulab.aulabchronicle.services;

import it.aulab.aulabchronicle.models.CareerRequest;
import it.aulab.aulabchronicle.models.User;

public interface CareerRequestService {
    boolean isRoleAlredyAssigned(User user,CareerRequest careerRequest);
    void save(CareerRequest careerRequest,User user);
    void careerAccept(Long requestId);
    CareerRequest find(Long id);
    
}
