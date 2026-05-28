package pe.edu.utp.prisma_api.domain.kanban;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.user.User;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kanbans")
public class Kanban {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "kanban_id")
    private List<ColumnKanban> columns = new ArrayList<>();
}
