package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;

@Getter
@Setter
public class KanbanDetailResponse {
    private UUID id;
    private String name;
    private boolean isPrivate;

    private UUID creatorId;
    private UUID projectId;
    private UUID teamId;

    private List<ColumnKanbanDTO> columns;

    private List<MilestoneSummaryResponse> milestones;
}
