package it.aulab.aulabchronicle.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
// Modifica il return type da User a Optional<User>
// Optional<User> findByUsername(String username);
// Mantieni gli altri metodi se servono
// Optional<User> findByEmail(String email);
// List<User> findByEmailIgnoreCaseContaining(String email);
// List<User> findByUsernameIgnoreCaseContaining(String username);
// Optional<User> findByUsername(String username);
// Optional<User> findByEmailIgnoreCase(String email);
// Optional<User> findOneByUsername(String username);
// Optional<User> findByEmail(String email);

User findByEmail(String email);
   
}

