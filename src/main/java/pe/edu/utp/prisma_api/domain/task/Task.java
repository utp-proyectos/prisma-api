package pe.edu.utp.prisma_api.domain.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;
import pe.edu.utp.prisma_api.domain.checklist.Checklist;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.milestone.Milestone;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private String id;

  @Column(nullable = false)
  private String title;

  @Column
  private String description;

  @Column(nullable = false)
  private Integer position;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "is_group_task", nullable = false)
  private boolean isGroupTask;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "column_id", nullable = false)
  private ColumnKanban column;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "milestone_id")
  private Milestone milestone;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TaskAssignment> assignments = new ArrayList<>();

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Checklist> checklists = new ArrayList<>();
}