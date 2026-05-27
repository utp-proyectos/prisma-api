package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.task.Task;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns")
public class ColumnKanban {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "position")
    private Integer position;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ColumnType type;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "columnKanban_id")
    private List<Task> tasks;
}
