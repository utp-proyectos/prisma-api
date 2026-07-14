package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class UpdateColumnKanbanDTO {
    private UUID columnId;
    private String title;
}