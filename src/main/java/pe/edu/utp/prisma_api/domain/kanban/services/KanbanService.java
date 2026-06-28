package pe.edu.utp.prisma_api.domain.kanban.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;

public interface KanbanService {
    List<KanbanDTO> findAllByProjectId(UUID projectId);

    Optional<KanbanDTO> findById(UUID id);

    KanbanDTO save(UUID projectId, UUID creadtorId, CreateKanbanDTO dto);

    Optional<KanbanDTO> update(UUID id, UpdateKanbanDTO dto);

    void delete(UUID id);
}
