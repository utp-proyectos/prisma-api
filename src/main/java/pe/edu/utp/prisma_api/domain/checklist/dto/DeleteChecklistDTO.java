package pe.edu.utp.prisma_api.domain.checklist.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class DeleteChecklistDTO {
    private UUID checklistId;
}
