package pe.edu.utp.prisma_api.domain.project.dto;

import lombok.Data;

@Data
public class CreateProjectDTO {

    private String name;

    private String description;

    private String coverImageUrl;

}