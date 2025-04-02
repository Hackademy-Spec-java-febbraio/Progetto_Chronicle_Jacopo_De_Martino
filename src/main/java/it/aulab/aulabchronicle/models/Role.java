package it.aulab.aulabchronicle.models;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
   
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
