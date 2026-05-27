package pe.edu.utp.prisma_api.domain.task;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;
import pe.edu.utp.prisma_api.domain.checklist.Checklist;
import pe.edu.utp.prisma_api.domain.user.User;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_group_task", nullable = false)
    private boolean isGroupTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @ManyToOne
    @Column(name = "created_by_id")
    private User createdBy;

    // Muchos a muchos
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignment> assignments;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<Checklist> checklists;
}
