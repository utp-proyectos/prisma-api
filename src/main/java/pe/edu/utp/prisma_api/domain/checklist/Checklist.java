package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checklists")
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id")
    private List<ChecklistItem> items;
}
