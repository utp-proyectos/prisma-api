package pe.edu.utp.prisma_api.domain.board;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import pe.edu.utp.prisma_api.domain.folder.Folder;

@Data
@Entity
@Table(name = "board")
public class Board {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "board_id")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "is_private", nullable = false)
  private boolean isPrivate;

  @ManyToOne
  @JoinColumn(name = "folder_id", nullable = true)
  private Folder folder;

  // @ManyToOne
  // @JoinColumn(name = "project_id", nullable = false)
  // private Project project; // The natural parent of the board

  // @ManyToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "creator_id", nullable = false)
  // private User creator;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "konva_data", columnDefinition = "jsonb")
  private String konvaData;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

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
