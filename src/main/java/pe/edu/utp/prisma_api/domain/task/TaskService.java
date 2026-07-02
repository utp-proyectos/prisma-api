package pe.edu.utp.prisma_api.domain.task;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanRepository;
import pe.edu.utp.prisma_api.domain.milestone.Milestone;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneRepository;
import pe.edu.utp.prisma_api.domain.task.dto.CreateTaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;
import pe.edu.utp.prisma_api.domain.task.dto.UpdateTaskDTO;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ColumnKanbanRepository columnRepository;
    private final MilestoneRepository milestoneRepository;
    private final UserRepository userRepository;
    private final TaskMapper mapper;

    public TaskDetailResponse save(CreateTaskDTO dto) {

        ColumnKanban column = columnRepository.findById(dto.getColumnId())
                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrada"));

        Task task = mapper.toEntity(dto);

        task.setColumn(column);
        int nextPosition = taskRepository.countByColumnId(dto.getColumnId());
        task.setPosition(nextPosition);
        task.setCompleted(false);

        // Si pertenece a un hito
        if (dto.getMilestoneId() != null) {

            Milestone milestone = milestoneRepository.findById(dto.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado"));

            // Validar que el hito pertenezca al mismo tablero
            if (!milestone.getKanban().getId().equals(column.getKanban().getId())) {
                throw new IllegalArgumentException(
                        "El hito no pertenece al mismo tablero que la columna");
            }

            task.setMilestone(milestone);
        }

        Task saved = taskRepository.save(task);

        return mapper.toDetail(saved);
    }

    @Transactional
    public TaskDetailResponse update(UpdateTaskDTO dto) {

        Task task = taskRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));

        UUID oldColumnId = task.getColumn().getId();

        mapper.update(dto, task);

        // Logica de columnas
        if (dto.getColumnId() != null && !dto.getColumnId().equals(oldColumnId)) {
            ColumnKanban newColumn = columnRepository.findById(dto.getColumnId())
                    .orElseThrow(() -> new ResourceNotFoundException("Columna destino no encontrada"));

            task.setColumn(newColumn);

            int nextPosition = taskRepository.countByColumnId(dto.getColumnId());
            task.setPosition(nextPosition);
        }

        // Logica de hitos y fechas
        if (dto.getMilestoneId() != null) {

            Milestone milestone = milestoneRepository.findById(dto.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado"));

            if (!milestone.getKanban().getId().equals(task.getColumn().getKanban().getId())) {
                throw new IllegalArgumentException(
                        "El hito no pertenece al mismo tablero que la tarea");
            }

            task.setMilestone(milestone);
            task.setDeadline(milestone.getDeadline());

        } else if (dto.getDeadline() != null) {

            task.setMilestone(null);
            task.setDeadline(dto.getDeadline());

        } else if (dto.getMilestoneId() == null && dto.getDeadline() == null) {

            // Sin fecha
            task.setMilestone(null);
            task.setDeadline(null);
        }

        // Asignaciones
        if (dto.getAssignedUserIds() != null) {
            task.getAssignments().clear();

            // Si la tarea NO es grupal pero mandaron más de un id por error, nos quedamos
            // solo con el primero
            List<UUID> userIdsToAssign = dto.getAssignedUserIds();
            if (!task.isGroupTask() && userIdsToAssign.size() > 1) {
                userIdsToAssign = List.of(userIdsToAssign.get(0));
            }

            // 3. Creamos y vinculamos las nuevas asignaciones
            for (UUID userId : userIdsToAssign) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

                TaskAssignment assignment = new TaskAssignment();
                assignment.setTask(task);
                assignment.setUser(user);
                assignment.setDone(false);

                task.getAssignments().add(assignment);
            }
        }

        Task updated = taskRepository.save(task);

        return mapper.toDetail(updated);
    }
}
