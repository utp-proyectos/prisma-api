package pe.edu.utp.prisma_api.domain.project.services;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectHomeDTO;
import pe.edu.utp.prisma_api.domain.project.dto.UpdateProjectDTO;

public interface ProjectService {

    List<ProjectDTO> findRecentByUser(String userId);

    Optional<ProjectDTO> findById(String id);

    ProjectDTO save(String teamId, CreateProjectDTO dto);

    Optional<ProjectDTO> update(
            String id,
            UpdateProjectDTO dto);

    void delete(String id);

    ProjectHomeDTO getHomeSummary(String projectId);

}