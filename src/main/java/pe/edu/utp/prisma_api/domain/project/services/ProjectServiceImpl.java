package pe.edu.utp.prisma_api.domain.project.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectMapper;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectHomeDTO;
import pe.edu.utp.prisma_api.domain.project.dto.UpdateProjectDTO;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final TeamRepository teamRepository;

    private final TaskRepository taskRepository;

    private final ProjectMapper mapper;

    @Override
    public Optional<ProjectDTO> findById(String id) {
        return projectRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public ProjectDTO save(
            String teamId,
            CreateProjectDTO dto) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

        Project project = mapper.toEntity(dto);
        project.setTeam(team);

        return mapper.toDto(
                projectRepository.save(project));

    }

    @Override
    public Optional<ProjectDTO> update(
            String id,
            UpdateProjectDTO dto) {

        return projectRepository.findById(id)
                .map(project -> {

                    mapper.update(dto, project);

                    return mapper.toDto(
                            projectRepository.save(project));

                });

    }

    @Override
    public void delete(String id) {
        projectRepository.deleteById(id);
    }

    // Para obtener los proyectos recientes de un usuario
    @Override
    public List<ProjectDTO> findRecentByUser(String userId) {

        return mapper.toDto(
                projectRepository.findAllByUserId(userId));

    }

    @Override
    public ProjectHomeDTO getHomeSummary(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        ProjectHomeDTO home = new ProjectHomeDTO();
        home.setId(project.getId());
        home.setName(project.getName());
        home.setDescription(project.getDescription());

        // 1. Calcular métricas de Kanban
        // Contamos las tareas totales, las completadas y calculamos el porcentaje
        long total = taskRepository.countAllTasksByProjectId(projectId);
        long completed = taskRepository.countCompletedTasksByProjectId(projectId);
        long remaining = total - completed;

        double percentage = (total > 0) ? ((double) completed / total) * 100 : 0;

        home.setTotalTasks(total);
        home.setCompletedTasks(completed);
        home.setRemainingTasks(remaining);
        home.setCompletionPercentage(percentage);

        return home;
    }

}
