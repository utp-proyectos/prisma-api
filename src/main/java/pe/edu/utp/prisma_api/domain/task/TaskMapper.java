package pe.edu.utp.prisma_api.domain.task;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.checklist.ChecklistMapper;
import pe.edu.utp.prisma_api.domain.task.dto.CreateTaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.UpdateTaskDTO;

@Mapper(componentModel = "spring", uses = {
        ChecklistMapper.class,
        TaskAssignmentMapper.class
})
public interface TaskMapper {

    @Mapping(target = "milestoneId", source = "milestone.id")
    TaskDTO toDto(Task entity);

    List<TaskDTO> toDto(List<Task> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "column", ignore = true)
    @Mapping(target = "milestone", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "checklists", ignore = true)
    @Mapping(target = "position", ignore = true)
    Task toEntity(CreateTaskDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "column", ignore = true)
    @Mapping(target = "milestone", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "checklists", ignore = true)
    @Mapping(target = "position", ignore = true)
    void update(UpdateTaskDTO dto,
            @MappingTarget Task entity);
}
