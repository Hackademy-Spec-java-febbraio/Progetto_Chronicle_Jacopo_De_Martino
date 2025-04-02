package it.aulab.aulabchronicle.repositories;




import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.Category;

@Repository
public interface CategoryRepository extends ListCrudRepository<Category,Long> {
    
}
