package pe.edu.utp.prisma_api.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  private String token;
  private String id;
  private String name;
  private String lastName;
  private String username;
  private String email;
  private String avatar;
  private String role;
}