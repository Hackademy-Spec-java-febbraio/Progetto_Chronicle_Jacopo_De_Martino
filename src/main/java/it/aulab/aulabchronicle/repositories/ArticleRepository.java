package it.aulab.aulabchronicle.repositories;



import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.models.User;

@Repository
public interface ArticleRepository  extends ListCrudRepository<Article,Long>{
    List<Article> getByCategory(Category category);
    List<Article> findByUser(User user);


}
