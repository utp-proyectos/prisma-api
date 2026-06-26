package pe.edu.utp.prisma_api.domain.kanban.dto;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanResponseDTO;

import java.util.List;

import lombok.Data;

@Data
public class KanbanResponseDTO {
    private String id;
    private String name;
    private boolean isPrivate;
    private String creatorName;
    private String creatorId;
    private List<ColumnKanbanResponseDTO> columns;
}
