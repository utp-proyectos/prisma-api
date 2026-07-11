package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.task.TaskMapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ColumnKanbanMapper {

    @Mapping(target = "kanbanId", source = "kanban.id")
    @Mapping(target = "projectId", source = "kanban.project.id")
    @Mapping(target = "teamId", source = "kanban.project.team.id")
    ColumnKanbanDetailResponse toDetail(ColumnKanban entity);

    List<ColumnKanbanDetailResponse> toDetail(List<ColumnKanban> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "fixed", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "position", ignore = true)
    ColumnKanban toEntity(CreateColumnKanbanDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "fixed", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "position", ignore = true)
    void update(UpdateColumnKanbanDTO dto,
            @MappingTarget ColumnKanban entity);
}
