package pe.edu.utp.prisma_api.domain.team;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.project.Project;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "team")
  private List<TeamMember> members = new ArrayList<>();

  @OneToMany(mappedBy = "team")
  private List<Project> projects = new ArrayList<>();

  @CreationTimestamp
  private LocalDateTime createdAt;
}
