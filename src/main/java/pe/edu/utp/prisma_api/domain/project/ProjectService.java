package pe.edu.utp.prisma_api.domain.project;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.project.dto.ProjectHomeDTO;

public interface ProjectService {

    Optional<Project> get(String id);

    Project save(String teamId, Project project);

    Optional<Project> update(String id, Project project);

    void delete(String id);

    List<Project> getUserRecentProjects(String userId);

    List<Project> getProjectByTeamId(String teamId);

    ProjectHomeDTO getProjectHomeSummary(String projectId);
}