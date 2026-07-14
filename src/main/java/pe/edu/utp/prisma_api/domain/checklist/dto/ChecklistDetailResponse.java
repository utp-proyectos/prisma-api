package pe.edu.utp.prisma_api.domain.checklist.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.ChecklistItemResponse;

@Data
public class ChecklistDetailResponse {
    private String id;
    private String title;
    private Priority priority;
    private List<ChecklistItemResponse> items;

    private UUID taskId;
    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
