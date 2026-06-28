package pe.edu.utp.prisma_api.domain.invitation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.prisma_api.common.enums.TeamRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberRequest {
  @Email(message = "Email inválido")
  @NotBlank
  private String email;

  @NotNull
  private TeamRole role;
}
