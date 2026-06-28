package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.task.Task;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns")
public class ColumnKanban {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private boolean isFixed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ColumnType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id", nullable = false)
    private Kanban kanban;

    @OneToMany(mappedBy = "column")
    private List<Task> tasks = new ArrayList<>();
}