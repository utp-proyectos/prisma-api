package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.CreateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.UpdateChecklistItemDTO;

@Mapper(componentModel = "spring")
public interface ChecklistItemMapper {

    ChecklistItemDTO toDto(ChecklistItem entity);

    List<ChecklistItemDTO> toDto(List<ChecklistItem> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    @Mapping(target = "completed", ignore = true)
    ChecklistItem toEntity(CreateChecklistItemDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checklist", ignore = true)
    void update(UpdateChecklistItemDTO dto,
            @MappingTarget ChecklistItem entity);
}