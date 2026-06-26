package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnDTO;
import pe.edu.utp.prisma_api.domain.task.TaskMapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ColumnMapper {

    ColumnKanbanDTO toDto(ColumnKanban entity);

    List<ColumnKanbanDTO> toDto(List<ColumnKanban> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    ColumnKanban toEntity(CreateColumnDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void update(CreateColumnDTO dto,
            @MappingTarget ColumnKanban entity);
}
