package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanService;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class KanbanService {

        private final KanbanRepository kanbanRepository;
        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;
        private final KanbanMapper kanbanMapper;
        private final ColumnKanbanService columnKanbanService;

        public List<KanbanDTO> findAllByProjectId(UUID projectId) {
                return kanbanMapper.toDto(
                                kanbanRepository.findByProjectId(projectId));
        }

        public Optional<KanbanDetailResponse> findById(UUID id) {
                return kanbanRepository.findById(id)
                                .map(kanbanMapper::toDetail);
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

        public Optional<KanbanDTO> update(UUID id,
                        UpdateKanbanDTO dto) {

                return kanbanRepository.findById(id)
                                .map(existing -> {
                                        kanbanMapper.update(dto, existing);

                                        return kanbanMapper.toDto(
                                                        kanbanRepository.save(existing));

                                });

        }

        public void delete(UUID id) {
                kanbanRepository.deleteById(id);
        }
}
