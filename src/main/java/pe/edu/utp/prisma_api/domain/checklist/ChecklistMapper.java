package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.CreateChecklistDTO;

@Mapper(componentModel = "spring", uses = ChecklistItemMapper.class)
public interface ChecklistMapper {

    ChecklistDTO toDto(Checklist entity);

    List<ChecklistDTO> toDto(List<Checklist> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "items", ignore = true)
    Checklist toEntity(CreateChecklistDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "items", ignore = true)
    void update(CreateChecklistDTO dto,
            @MappingTarget Checklist entity);
}