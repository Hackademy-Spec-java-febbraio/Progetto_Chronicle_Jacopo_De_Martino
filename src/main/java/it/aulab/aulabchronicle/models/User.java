package it.aulab.aulabchronicle.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username",nullable = false,length = 30)
    private String username;
    @Column(name = "email", nullable = false,unique = true,length = 80)
    private String email;
    @Column(name="password",nullable = false,length =30)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",  // Nome della tabella di join (quella ponte)
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),  // Colonna che punta a questa entità (User)
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")  // Colonna che punta all'entità collegata (Role)
    )
    private List<Role> roles = new ArrayList<>();
}

