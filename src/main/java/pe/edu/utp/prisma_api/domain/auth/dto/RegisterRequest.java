package pe.edu.utp.prisma_api.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "El nombre es requerido")
  private String name;

  @NotBlank(message = "El apellido es requerido")
  private String lastName;

  @Email(message = "Email inválido")
  @NotBlank(message = "El email es requerido")
  private String email;

  @NotBlank(message = "El username es requerido")
  private String username;

  @NotBlank(message = "La contraseña es requerida")
  @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
  private String password;
}