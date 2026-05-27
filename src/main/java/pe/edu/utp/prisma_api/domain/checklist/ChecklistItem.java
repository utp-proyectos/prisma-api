package pe.edu.utp.prisma_api.domain.checklist;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checklist_items")
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;
}
