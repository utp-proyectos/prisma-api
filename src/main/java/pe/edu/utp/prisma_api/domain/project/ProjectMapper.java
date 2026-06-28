package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.kanban.KanbanMapper;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.UpdateProjectDTO;

@Mapper(componentModel = "spring", uses = KanbanMapper.class)
public interface ProjectMapper {

    @Mapping(target = "teamId", source = "team.id")
    ProjectDTO toDto(Project project);

    List<ProjectDTO> toDto(List<Project> projects);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "kanbans", ignore = true)
    @Mapping(target = "boards", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "channels", ignore = true)
    Project toEntity(CreateProjectDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "kanbans", ignore = true)
    @Mapping(target = "boards", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "channels", ignore = true)
    void update(UpdateProjectDTO dto, @MappingTarget Project project);
}