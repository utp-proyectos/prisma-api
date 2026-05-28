package pe.edu.utp.prisma_api.domain.team;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.project.Project;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  // Muchos a muchos con usuarios a través de TeamMember
  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
  private List<TeamMember> members;

  @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "team_id")
  private List<Project> projects;
}
