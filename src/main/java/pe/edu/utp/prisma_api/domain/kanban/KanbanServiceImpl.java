package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanRequestDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanResponseDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
public class KanbanServiceImpl implements KanbanService {

    @Autowired
    private KanbanRepository kanbanRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KanbanMapper kanbanMapper;

    // Para obtener los kanbans de un proyecto
    @Override
    public List<KanbanResponseDTO> findAllByProjectId(String projectId) {
        return projectRepository.findById(projectId)
                .map(project -> project.getKanbans().stream()
                        .map(kanbanMapper::toResponseDTO)
                        .toList())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    // Para obtener un kanban específico con sus columnas y tareas
    @Override
    public Optional<KanbanResponseDTO> get(String id) {
        return kanbanRepository.findById(id).map(kanbanMapper::toResponseDTO);
    }

    @Override
    public KanbanResponseDTO save(String projectId, String creatorId, KanbanRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Convertimos DTO a Entidad limpia
        Kanban kanban = kanbanMapper.toEntity(dto);
        kanban.setCreator(creator);

        // Inicializamos las columnas básicas por defecto
        // kanban.setColumns(createDefaultColumns());

        // Al ser unidireccional, el Proyecto es el que guarda al Kanban
        project.getKanbans().add(kanban);
        projectRepository.save(project);

        return kanbanMapper.toResponseDTO(kanban);
    }

    @Override
    public Optional<KanbanResponseDTO> update(String id, KanbanRequestDTO dto) {
        return kanbanRepository.findById(id).map(existingKanban -> {
            // Actualizamos solo los campos permitidos del DTO
            kanbanMapper.updateEntityFromDto(dto, existingKanban);
            return kanbanMapper.toResponseDTO(kanbanRepository.save(existingKanban));
        });
    }

    @Override
    public void delete(String id) {
        kanbanRepository.deleteById(id);
    }
}
