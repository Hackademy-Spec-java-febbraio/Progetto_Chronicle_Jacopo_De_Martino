package it.aulab.aulabchronicle.models;



import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false,length = 100)
    @NotEmpty
    @Size(max = 100)
    private String title;
    @Column(name = "subtitle",nullable = false,length = 100)
    @NotEmpty
    @Size(max = 100)
    private String subtitle;

    @Column(name = "body",nullable=false,length=1000)
    private String body;

    @Column(name="publish_date",nullable=true,length=8)
    @NotNull
    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnoreProperties({"articles"})
    private User user;

    @ManyToOne
    @JoinColumn(name="category_id")
    @JsonIgnoreProperties({"articles"})
    private Category category;

    @OneToOne(mappedBy="article")
    @JsonIgnoreProperties({"article"})
    private Image image;

    
}
