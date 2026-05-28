package pe.edu.utp.prisma_api.domain.project;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectHomeDTO;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Optional<Project> get(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public Project save(String teamId, Project project) {
        return teamRepository.findById(teamId).map(team -> {
            team.getProjects().add(project);
            teamRepository.save(team);
            return project;
        }).orElseThrow(() -> new RuntimeException("Error: Equipo no encontrado para asignar el proyecto"));
    }

    @Override
    public Optional<Project> update(String id, Project projectDetails) {
        return projectRepository.findById(id).map(existingProject -> {
            existingProject.setName(projectDetails.getName());
            existingProject.setDescription(projectDetails.getDescription());
            existingProject.setCoverImageUrl(projectDetails.getCoverImageUrl());
            return projectRepository.save(existingProject);
        });
    }

    @Override
    public void delete(String id) {
        projectRepository.deleteById(id);
    }

    // Para obtener los proyectos recientes de un usuario
    @Override
    public List<Project> getUserRecentProjects(String userId) {
        List<Project> projects = projectRepository.findAllByUserId(userId);
        return projects;
    }

    // Para obtener proyectos por equipo
    @Override
    public List<Project> getProjectByTeamId(String teamId) {
        return projectRepository.findByTeamId(teamId);
    }

    @Override
    public ProjectHomeDTO getProjectHomeSummary(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

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
