package pe.edu.utp.prisma_api.domain.milestone;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDTO;
import pe.edu.utp.prisma_api.domain.task.TaskMapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface MilestoneMapper {

    MilestoneDTO toDto(Milestone entity);

    List<MilestoneDTO> toDto(List<Milestone> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Milestone toEntity(CreateMilestoneDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void update(CreateMilestoneDTO dto,
            @MappingTarget Milestone entity);
}
