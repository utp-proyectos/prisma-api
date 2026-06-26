package pe.edu.utp.prisma_api.domain.kanban;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanRequestDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanResponseDTO;

@Mapper(componentModel = "spring")
public interface KanbanMapper {

    @Mapping(target = "creatorName", source = "creator.name")
    @Mapping(target = "creatorId", source = "creator.id")
    KanbanResponseDTO toResponseDTO(Kanban kanban);

    // ignoramos el creador: seteamos en el Service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "columns", ignore = true)
    Kanban toEntity(KanbanRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "columns", ignore = true)
    void updateEntityFromDto(KanbanRequestDTO dto, @MappingTarget Kanban kanban);
}
