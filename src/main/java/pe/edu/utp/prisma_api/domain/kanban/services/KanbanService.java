package pe.edu.utp.prisma_api.domain.kanban.services;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;

public interface KanbanService {
    List<KanbanDTO> findAllByProjectId(String projectId);

    Optional<KanbanDTO> findById(String id);

    KanbanDTO save(String projectId, String creadtorId, CreateKanbanDTO dto);

    Optional<KanbanDTO> update(String id, UpdateKanbanDTO dto);

    void delete(String id);
}
