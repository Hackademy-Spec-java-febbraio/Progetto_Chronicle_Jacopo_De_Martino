package it.aulab.aulabchronicle.dtos;

import java.time.LocalDate;

import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.models.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private User user;
    private Category category;
    private Image image;
    
}
