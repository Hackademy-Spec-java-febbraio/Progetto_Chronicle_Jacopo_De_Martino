package it.aulab.aulabchronicle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.aulab.aulabchronicle.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long>{

    @Modifying
    @Query(value = "DELETE FROM images WHERE path = :path", nativeQuery = true)
    void deleteByPath(@Param("path") String path);


}
