package pe.edu.utp.prisma_api.domain.columnKanban.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;

public interface ColumnKanbanService {
    List<ColumnKanbanDTO> findAllByKanban(UUID kanbanId);

    Optional<ColumnKanbanDTO> findById(UUID id);

    ColumnKanbanDTO save(UUID kanbanId, CreateColumnKanbanDTO dto);

    Optional<ColumnKanbanDTO> update(UUID id, UpdateColumnKanbanDTO dto);

    void delete(UUID id);

    void reorder(UUID kanbanId, List<UUID> columnIds);
}