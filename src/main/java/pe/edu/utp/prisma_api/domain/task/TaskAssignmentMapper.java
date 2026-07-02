package pe.edu.utp.prisma_api.domain.task;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.task.dto.CreateTaskAssignmentDTO;
import pe.edu.utp.prisma_api.domain.task.dto.TaskAssignmentDTO;

@Mapper(componentModel = "spring")
public interface TaskAssignmentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "lastName", source = "user.lastName")
    TaskAssignmentDTO toDto(TaskAssignment entity);

    List<TaskAssignmentDTO> toDto(List<TaskAssignment> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "done", ignore = true)
    TaskAssignment toEntity(CreateTaskAssignmentDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "done", ignore = true)
    void update(CreateTaskAssignmentDTO dto,
            @MappingTarget TaskAssignment entity);
}
