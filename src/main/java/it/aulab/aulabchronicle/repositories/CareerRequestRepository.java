package it.aulab.aulabchronicle.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.CareerRequest;

@Repository
public interface CareerRequestRepository extends CrudRepository<CareerRequest,Long> {
    // @Query(value = "SELECT * FROM career_request WHERE is_checked = 0", nativeQuery = true)
    List<CareerRequest> findByIsCheckedFalse();
    @Query(value = "SELECT user_id FROM user_roles", nativeQuery = true)
    List<Long> findAllUserIds();
    @Query(value ="SELECT role_id FROM user_roles WHERE user_id =:id", nativeQuery =true)
    List<Long> findByUserId(@Param("id") Long id);
    
}
