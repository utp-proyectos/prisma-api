package pe.edu.utp.prisma_api.domain.project;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.exception.UnauthorizedException;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public ProjectResponse createProject(UUID teamId, CreateProjectRequest request, UUID userId) {

        boolean isMember = teamMemberRepository.existsByUserIdAndTeamId(userId, teamId);

        if (!isMember) {
            throw new UnauthorizedException("No perteneces a este equipo");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTeam(team);
        projectRepository.save(project);

        return toDto(project);
    }

    private ProjectResponse toDto(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt());
    }
}