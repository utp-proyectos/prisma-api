package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import lombok.Data;

@Data
public class UpdateColumnDTO {
    private String title;
    private Integer position;
}