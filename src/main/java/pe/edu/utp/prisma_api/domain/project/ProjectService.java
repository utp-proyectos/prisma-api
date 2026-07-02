package pe.edu.utp.prisma_api.domain.project;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.channel.Channel;
import pe.edu.utp.prisma_api.domain.channel.ChannelRepository;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final ChannelRepository channelRepository;

    public ProjectResponse createProject(CreateProjectRequest request) {
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTeam(team);
        projectRepository.save(project);

        Channel channel = new Channel();
        channel.setName("General");
        channel.setProject(project);

        channelRepository.save(channel);

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