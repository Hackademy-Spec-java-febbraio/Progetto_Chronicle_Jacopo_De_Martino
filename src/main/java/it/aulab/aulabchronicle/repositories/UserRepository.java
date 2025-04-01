package it.aulab.aulabchronicle.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmailIgnoreCaseContaining(String email);
    List<User> findByUsernameIgnoreCaseContaining(String username);
    User findByUsername(String username);
    User findByEmailIgnoreCase(String email);
    Optional<User> findOneByUsername(String username);
   
}

