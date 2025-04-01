package it.aulab.aulabchronicle.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "Fristname can't be empty")
    private String fristname;
    @NotEmpty(message = "Lastname can't be empty")
    private String lastname;
    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;
    @NotEmpty(message = "Password ... inserire una password valida")
    private String password;

}
