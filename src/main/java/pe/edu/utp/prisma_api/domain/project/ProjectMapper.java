package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import org.mapstruct.Mapper;

import pe.edu.utp.prisma_api.domain.kanban.KanbanMapper;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;

@Mapper(componentModel = "spring", uses = KanbanMapper.class)
public interface ProjectMapper {
    ProjectResponse toDto(Project project);

    List<ProjectResponse> toDtoList(List<Project> projects);
}