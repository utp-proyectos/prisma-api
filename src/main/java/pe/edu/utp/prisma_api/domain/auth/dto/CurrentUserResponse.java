package pe.edu.utp.prisma_api.domain.auth.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponse {
  private UUID id;
  private String name;
  private String lastName;
  private String username;
  private String email;
  private String avatar;
  private String role;
}
