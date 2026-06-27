package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanMapper;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneMapper;

@Mapper(componentModel = "spring", uses = {
                ColumnKanbanMapper.class,
                MilestoneMapper.class
})
public interface KanbanMapper {

        @Mapping(target = "creatorId", source = "creator.id")
        KanbanDTO toDto(Kanban kanban);

        List<KanbanDTO> toDto(List<Kanban> kanbans);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "creator", ignore = true)
        @Mapping(target = "columns", ignore = true)
        @Mapping(target = "milestones", ignore = true)
        @Mapping(target = "project", ignore = true)
        Kanban toEntity(CreateKanbanDTO dto);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "creator", ignore = true)
        @Mapping(target = "columns", ignore = true)
        @Mapping(target = "milestones", ignore = true)
        @Mapping(target = "project", ignore = true)
        void update(UpdateKanbanDTO dto,
                        @MappingTarget Kanban entity);
}