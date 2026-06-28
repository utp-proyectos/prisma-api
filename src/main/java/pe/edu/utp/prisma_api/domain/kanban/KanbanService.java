package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanRequestDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanResponseDTO;

public interface KanbanService {
    // Para el Sidebar: Listar tableros de un proyecto (ID y Nombre)
    List<KanbanResponseDTO> findAllByProjectId(UUID projectId);

    // Para la vista principal: Obtener el tablero con todas sus columnas y tareas
    Optional<KanbanResponseDTO> get(String id);

    // Operaciones de escritura
    KanbanResponseDTO save(UUID projectId, UUID creatorId, KanbanRequestDTO dto);

    Optional<KanbanResponseDTO> update(String id, KanbanRequestDTO dto);

    void delete(String id);
}
