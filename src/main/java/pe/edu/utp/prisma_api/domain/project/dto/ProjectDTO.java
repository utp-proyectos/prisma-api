package pe.edu.utp.prisma_api.domain.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;

@Data
public class ProjectDTO {

    private String id;

    private String name;

    private String description;

    private String coverImageUrl;

    private LocalDateTime createdAt;

    private String teamId;

    private List<KanbanDTO> kanbans;

}