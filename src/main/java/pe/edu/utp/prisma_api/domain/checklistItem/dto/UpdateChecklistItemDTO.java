package pe.edu.utp.prisma_api.domain.checklistItem.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UpdateChecklistItemDTO {
    private UUID checklistItemId;
    private String content;
    private boolean completedItem;
}
