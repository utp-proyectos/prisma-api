package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.List;
import lombok.Data;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;

@Data
public class KanbanDTO {

    private String id;

    private String name;

    private boolean isPrivate;

    private String creatorId;

    private List<ColumnKanbanDTO> columns;
}