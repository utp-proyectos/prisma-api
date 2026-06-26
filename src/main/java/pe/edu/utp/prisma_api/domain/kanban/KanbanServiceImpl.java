package pe.edu.utp.prisma_api.domain.kanban;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
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
    public List<KanbanDTO> findAllByProjectId(String projectId) {
        return kanbanMapper.toDto(
                kanbanRepository.findByProjectId(projectId));
    }

    @Override
    public Optional<KanbanDTO> findById(String id) {
        return kanbanRepository.findById(id)
                .map(kanbanMapper::toDto);
    }

    @Override
    public KanbanDTO save(String projectId, String creatorId,
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
    public Optional<KanbanDTO> update(String id,
            UpdateKanbanDTO dto) {

        return kanbanRepository.findById(id)
                .map(existing -> {
                    kanbanMapper.update(dto, existing);

                    return kanbanMapper.toDto(
                            kanbanRepository.save(existing));

                });

    }

    @Override
    public void delete(String id) {
        kanbanRepository.deleteById(id);
    }

    // Creamos una columna por defecto
    private List<ColumnKanban> createDefaultColumns(Kanban kanban) {

        List<ColumnKanban> columns = new ArrayList<>();

        columns.add(createColumn(
                "Pendiente",
                ColumnType.PENDING,
                1,
                kanban));

        columns.add(createColumn(
                "En curso",
                ColumnType.IN_PROGRESS,
                2,
                kanban));

        columns.add(createColumn(
                "Completado",
                ColumnType.COMPLETED,
                3,
                kanban));

        return columns;
    }

    private ColumnKanban createColumn(String title,
            ColumnType type,
            Integer position,
            Kanban kanban) {

        ColumnKanban column = new ColumnKanban();

        column.setTitle(title);
        column.setType(type);
        column.setPosition(position);
        column.setKanban(kanban);
        column.setTasks(new ArrayList<>());

        return column;
    }
}
