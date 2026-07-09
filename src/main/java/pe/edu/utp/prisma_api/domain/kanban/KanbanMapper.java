package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanMapper;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneMapper;

@Mapper(componentModel = "spring", uses = {
                ColumnKanbanMapper.class,
                MilestoneMapper.class
})
public interface KanbanMapper {

        @Mapping(target = "creatorId", source = "creator.id")
        @Mapping(target = "projectId", source = "project.id")
        @Mapping(target = "teamId", source = "project.team.id")
        @Mapping(target = "privateSwitch", source = "private")
        KanbanDTO toDto(Kanban kanban);

        List<KanbanDTO> toDto(List<Kanban> kanbans);

        // Para los detalles del tablero
        @Mapping(target = "creatorId", source = "creator.id")
        @Mapping(target = "projectId", source = "project.id")
        @Mapping(target = "teamId", source = "project.team.id")
        @Mapping(target = "privateSwitch", source = "private")
        KanbanDetailResponse toDetail(Kanban kanban);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "creator", ignore = true)
        @Mapping(target = "columns", ignore = true)
        @Mapping(target = "milestones", ignore = true)
        @Mapping(target = "project", ignore = true)
        @Mapping(target = "private", source = "privateSwitch")
        Kanban toEntity(CreateKanbanDTO dto);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "creator", ignore = true)
        @Mapping(target = "columns", ignore = true)
        @Mapping(target = "milestones", ignore = true)
        @Mapping(target = "project", ignore = true)
        @Mapping(target = "private", source = "privateSwitch")
        void update(UpdateKanbanDTO dto,
                        @MappingTarget Kanban entity);
}