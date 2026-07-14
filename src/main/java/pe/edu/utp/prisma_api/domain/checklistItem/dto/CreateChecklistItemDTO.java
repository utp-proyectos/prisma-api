package pe.edu.utp.prisma_api.domain.checklistItem.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateChecklistItemDTO {
    private UUID checklistId;
    private String content;
}
