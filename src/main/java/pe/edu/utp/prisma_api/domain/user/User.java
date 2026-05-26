package pe.edu.utp.prisma_api.domain.user;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.AuthProvider;
import pe.edu.utp.prisma_api.common.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;

  private String email;

  private String password;

  private String avatar;

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private boolean emailVerified;

  @Enumerated(EnumType.STRING)
  private Role role;

  private LocalDate createdAt;

  private LocalDate updatedAt;

}
