package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.task.TaskMapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ColumnKanbanMapper {

    ColumnKanbanDTO toDto(ColumnKanban entity);

    List<ColumnKanbanDTO> toDto(List<ColumnKanban> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    ColumnKanban toEntity(CreateColumnKanbanDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "fixed", ignore = true)
    @Mapping(target = "type", ignore = true)
    void update(UpdateColumnKanbanDTO dto,
            @MappingTarget ColumnKanban entity);
}
