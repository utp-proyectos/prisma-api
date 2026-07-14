package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDetailResponse;
import pe.edu.utp.prisma_api.domain.checklist.dto.CreateChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.UpdateChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.ChecklistItemMapper;

@Mapper(componentModel = "spring", uses = ChecklistItemMapper.class)
public interface ChecklistMapper {

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "kanbanId", source = "task.column.kanban.id")
    @Mapping(target = "projectId", source = "task.column.kanban.project.id")
    @Mapping(target = "teamId", source = "task.column.kanban.project.team.id")
    ChecklistDetailResponse toDto(Checklist entity);

    List<ChecklistDetailResponse> toDto(List<Checklist> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "items", ignore = true)
    Checklist toEntity(CreateChecklistDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "items", ignore = true)
    void update(UpdateChecklistDTO dto,
            @MappingTarget Checklist entity);
}