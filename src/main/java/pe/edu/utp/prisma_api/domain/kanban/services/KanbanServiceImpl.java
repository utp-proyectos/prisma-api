package pe.edu.utp.prisma_api.domain.kanban.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanMapper;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class KanbanServiceImpl implements KanbanService {

        private final KanbanRepository kanbanRepository;

        private final ProjectRepository projectRepository;

        private final UserRepository userRepository;

        private final KanbanMapper kanbanMapper;

        @Override
        public List<KanbanDTO> findAllByProjectId(UUID projectId) {
                return kanbanMapper.toDto(
                                kanbanRepository.findByProjectId(projectId));
        }

        @Override
        public Optional<KanbanDTO> findById(UUID id) {
                return kanbanRepository.findById(id)
                                .map(kanbanMapper::toDto);
        }

        @Override
        public KanbanDTO save(UUID projectId, UUID creatorId,
                        CreateKanbanDTO dto) {

                Project project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

                User creator = userRepository.findById(creatorId).orElseThrow(
                                () -> new ResourceNotFoundException("Usuario no encontrado"));

                Kanban kanban = kanbanMapper.toEntity(dto);
                kanban.setCreator(creator);
                kanban.setProject(project);
                kanban.initializeDefaultBoard();

                return kanbanMapper.toDto(kanbanRepository.save(kanban));
        }

        @Override
        public Optional<KanbanDTO> update(UUID id,
                        UpdateKanbanDTO dto) {

                return kanbanRepository.findById(id)
                                .map(existing -> {
                                        kanbanMapper.update(dto, existing);

                                        return kanbanMapper.toDto(
                                                        kanbanRepository.save(existing));

                                });

        }

        @Override
        public void delete(UUID id) {
                kanbanRepository.deleteById(id);
        }
}
