package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private List<Kanban> kanbans;
}
