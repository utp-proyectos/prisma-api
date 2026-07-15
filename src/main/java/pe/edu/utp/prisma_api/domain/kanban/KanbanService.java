package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanService;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanMyTasksResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.task.Task;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class KanbanService {

        private final KanbanRepository kanbanRepository;
        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;
        private final KanbanMapper kanbanMapper;
        private final ColumnKanbanService columnKanbanService;

        public List<KanbanDTO> findAllByProjectId(UUID projectId, UUID userId) {
                return kanbanMapper.toDto(
                                kanbanRepository.findByProjectIdAndAuthorizedUser(projectId, userId));
        }

        public List<KanbanMyTasksResponse> getMyKanbans(UUID userId) {

                System.out.println("User ID: " + userId);
                List<Task> myTasks = taskRepository.findTasksByUserIdWithKanban(userId);

                Map<Kanban, List<Task>> tasksByKanban = myTasks.stream()
                                .collect(Collectors.groupingBy(task -> task.getColumn().getKanban()));

                return tasksByKanban.entrySet().stream()
                                .map(entry -> kanbanMapper.toMyTasksResponse(entry.getKey(), entry.getValue()))
                                .toList();
        }

        public Optional<KanbanDetailResponse> findById(UUID id, UUID userId) {
                return kanbanRepository.findById(id)
                                .map(kanban -> {

                                        if (kanban.isPrivate() && !kanban.getCreator().getId().equals(userId)) {
                                                throw new AccessDeniedException(
                                                                "No tienes permisos para ver este tablero privado");
                                        }
                                        return kanbanMapper.toDetail(kanban);
                                });
        }

        public KanbanDTO save(UUID projectId, UUID creatorId,
                        CreateKanbanDTO dto) {

                Project project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

                User creator = userRepository.findById(creatorId).orElseThrow(
                                () -> new ResourceNotFoundException("Usuario no encontrado"));

                Kanban kanban = kanbanMapper.toEntity(dto);

                kanban.setCreator(creator);
                kanban.setProject(project);

                columnKanbanService.createDefaultColumns(kanban);

                kanban = kanbanRepository.save(kanban);

                return kanbanMapper.toDto(kanban);
        }

        public Optional<KanbanDTO> update(UUID id, UpdateKanbanDTO dto) {
                return kanbanRepository.findWithProjectAndTeamById(id)
                                .map(existing -> {

                                        kanbanMapper.update(dto, existing);
                                        Kanban guardado = kanbanRepository.save(existing);
                                        return kanbanMapper.toDto(guardado);
                                });
        }

        public void delete(UUID id) {
                kanbanRepository.deleteById(id);
        }
}
