package pe.edu.utp.prisma_api.domain.kanban;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.milestone.Milestone;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.user.User;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kanbans")
public class Kanban {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "kanban")
    private List<ColumnKanban> columns = new ArrayList<>();

    @OneToMany(mappedBy = "kanban")
    private List<Milestone> milestones = new ArrayList<>();

    public void addColumn(String title, ColumnType type, Integer position, Boolean isFixed) {
        ColumnKanban column = new ColumnKanban();
        column.setTitle(title);
        column.setType(type);
        column.setPosition(position);
        column.setFixed(isFixed);
        column.setKanban(this);
        column.setTasks(new ArrayList<>());
        this.columns.add(column);
    }

    public void initializeDefaultBoard() {
        this.columns = new ArrayList<>();
        this.milestones = new ArrayList<>();

        this.addColumn("Pendiente", ColumnType.PENDING, 1, false);
        this.addColumn("En curso", ColumnType.IN_PROGRESS, 2, false);
        this.addColumn("Completado", ColumnType.COMPLETED, 3, true);
    }
}
