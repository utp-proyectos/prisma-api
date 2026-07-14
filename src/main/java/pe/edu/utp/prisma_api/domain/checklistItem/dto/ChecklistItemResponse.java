package pe.edu.utp.prisma_api.domain.checklistItem.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ChecklistItemResponse {
    private String id;
    private String content;
    private boolean completedItem;

    private UUID checklistId;
    private UUID taskId;
    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
