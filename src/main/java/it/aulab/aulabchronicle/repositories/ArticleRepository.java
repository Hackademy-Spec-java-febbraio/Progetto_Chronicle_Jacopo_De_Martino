package it.aulab.aulabchronicle.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.models.User;

@Repository
public interface ArticleRepository  extends ListCrudRepository<Article,Long>{
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedIsTrue();
    List<Article> findByIsAcceptedIsFalse();
    List<Article> findByIsAcceptedIsNull();

    //! Query per ricerca dinamica
    @Query("SELECT a FROM Article a WHERE " +
       "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.body) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(FUNCTION('DATE_FORMAT', a.publishDate, '%d/%m/%Y')) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        List<Article> findBySearchTerm(@Param("searchTerm") String searchTerm);

}
