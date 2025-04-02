package it.aulab.aulabchronicle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long>{
    Role findByNameIgnoreCase(String name);

    Role findByName(String name);
}