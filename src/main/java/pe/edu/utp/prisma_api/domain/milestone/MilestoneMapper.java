package pe.edu.utp.prisma_api.domain.milestone;

import java.time.LocalDate;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;
import pe.edu.utp.prisma_api.domain.milestone.enums.StateMilestone;
import pe.edu.utp.prisma_api.domain.task.TaskMapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface MilestoneMapper {

    @Mapping(target = "progress", expression = "java(calculateProgress(entity))")
    @Mapping(target = "totalTasks", expression = "java(totalTasks(entity))")
    @Mapping(target = "completedTasks", expression = "java(completedTasks(entity))")
    @Mapping(target = "state", expression = "java(calculateState(entity))")
    @Mapping(target = "kanbanId", source = "kanban.id")
    MilestoneSummaryResponse toDto(Milestone entity);

    List<MilestoneSummaryResponse> toDto(List<Milestone> entities);

    @Mapping(target = "progress", expression = "java(calculateProgress(entity))")
    @Mapping(target = "totalTasks", expression = "java(totalTasks(entity))")
    @Mapping(target = "completedTasks", expression = "java(completedTasks(entity))")
    @Mapping(target = "state", expression = "java(calculateState(entity))")
    @Mapping(target = "kanbanId", source = "kanban.id")
    MilestoneDetailResponse toDetail(Milestone milestone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Milestone toEntity(CreateMilestoneDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kanban", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void update(CreateMilestoneDTO dto,
            @MappingTarget Milestone entity);

    default Integer totalTasks(Milestone milestone) {
        return milestone.getTasks().size();
    }

    default Integer completedTasks(Milestone milestone) {
        if (milestone.getTasks() == null)
            return 0;

        return (int) milestone.getTasks()
                .stream()
                .filter(task -> task != null && task.isCompleted())
                .count();
    }

    default Integer calculateProgress(Milestone milestone) {

        int total = totalTasks(milestone);

        if (total == 0) {
            return 0;
        }

        return completedTasks(milestone) * 100 / total;
    }

    default StateMilestone calculateState(Milestone milestone) {

        if (calculateProgress(milestone) == 100) {
            return StateMilestone.COMPLETADO;
        }

        if (milestone.getDeadline().isBefore(LocalDate.now())) {
            return StateMilestone.RETRASADO;
        }

        return StateMilestone.A_TIEMPO;
    }
}
