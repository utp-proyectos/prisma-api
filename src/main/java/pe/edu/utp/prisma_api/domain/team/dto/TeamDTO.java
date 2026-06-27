package pe.edu.utp.prisma_api.domain.team.dto;

import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDTO;

@Data
public class TeamDTO {

    private String id;

    private String name;

    private List<ProjectDTO> projects;

    private List<TeamMemberDTO> members;
}