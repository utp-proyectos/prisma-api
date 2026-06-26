package pe.edu.utp.prisma_api.domain.kanban.dto;

import lombok.Data;

@Data
public class CreateKanbanDTO {
    private String name;
    private boolean isPrivate;
}