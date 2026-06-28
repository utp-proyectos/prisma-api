package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;

@Data
public class KanbanDTO {

    private UUID id;

    private String name;

    private boolean isPrivate;

    private UUID creatorId;

    private List<ColumnKanbanDTO> columns;
}