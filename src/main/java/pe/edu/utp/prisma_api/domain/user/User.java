package pe.edu.utp.prisma_api.domain.user;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.AuthProvider;
import pe.edu.utp.prisma_api.common.enums.Role;
import pe.edu.utp.prisma_api.domain.task.TaskAssignment;
import pe.edu.utp.prisma_api.domain.team.TeamMember;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "avatar")
  private String avatar;

  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  @Column(name = "email_verified")
  private boolean emailVerified;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "created_at", nullable = false)
  private LocalDate createdAt;

  @Column(name = "updated_at")
  private LocalDate updatedAt;

  // Muchos a muchos
  @OneToMany(mappedBy = "user")
  private List<TeamMember> members;

  // Muchos a muchos
  @OneToMany(mappedBy = "user")
  private List<TaskAssignment> assignments;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDate.now();
    this.updatedAt = LocalDate.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDate.now();
  }

}
