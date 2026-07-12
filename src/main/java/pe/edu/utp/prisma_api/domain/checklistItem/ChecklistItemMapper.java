package pe.edu.utp.prisma_api.domain.checklistItem;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.checklistItem.dto.CreateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.UpdateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.ChecklistItemResponse;

@Mapper(componentModel = "spring")
public interface ChecklistItemMapper {

    @Mapping(target = "checklistId", source = "checklist.id")
    @Mapping(target = "taskId", source = "checklist.task.id")
    @Mapping(target = "projectId", source = "checklist.task.column.kanban.project.id")
    @Mapping(target = "teamId", source = "checklist.task.column.kanban.project.team.id")
    @Mapping(target = "kanbanId", source = "checklist.task.column.kanban.id")
    @Mapping(target = "completedItem", source = "completed")
    ChecklistItemResponse toDto(ChecklistItem entity);

    List<ChecklistItemResponse> toDto(List<ChecklistItem> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "completed", ignore = true)
    ChecklistItem toEntity(CreateChecklistItemDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "completed", source = "completedItem")
    void update(UpdateChecklistItemDTO dto,
            @MappingTarget ChecklistItem entity);
}