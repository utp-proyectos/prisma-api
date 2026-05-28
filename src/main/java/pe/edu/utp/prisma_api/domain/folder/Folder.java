package pe.edu.utp.prisma_api.domain.folder;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

import pe.edu.utp.prisma_api.domain.board.Board;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.team.TeamMember;

@Data
@Entity
@Table(name = "folder")
public class Folder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "folder_id")
  private String id;

  @Column(nullable = false)
  private String nombre; // nombre
  @Column(name = "is_private", nullable = false)
  private boolean isPrivate;

  @ManyToOne
  @JoinColumn(name = "creator_id", nullable = false)
  private TeamMember creator;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Board> boards;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
