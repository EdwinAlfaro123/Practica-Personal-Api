package PracticaPersonal2.PracticaPersonal2.Model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDTO {
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede ir vacio")
    @NotNull
    private String username;

    @Email
    @NotNull
    @NotBlank(message = "El correo no puede ir vacio")
    private String email;

    @NotNull
    @NotBlank(message = "La contraseña no puede ir vacia")
    private String password;
}
