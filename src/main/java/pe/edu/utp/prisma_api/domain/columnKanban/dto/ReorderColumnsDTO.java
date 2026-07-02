package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
public class ReorderColumnsDTO {
    private UUID kanbanId;
    private List<ColumnOrderDTO> columns;
    private UUID teamId;
    private UUID projectId;
}