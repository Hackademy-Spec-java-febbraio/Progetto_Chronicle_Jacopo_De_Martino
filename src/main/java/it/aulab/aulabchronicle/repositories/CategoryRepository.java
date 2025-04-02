package it.aulab.aulabchronicle.repositories;




import org.springframework.data.repository.ListCrudRepository;

import it.aulab.aulabchronicle.models.Category;

public interface CategoryRepository extends ListCrudRepository<Category,Long> {
    
}
