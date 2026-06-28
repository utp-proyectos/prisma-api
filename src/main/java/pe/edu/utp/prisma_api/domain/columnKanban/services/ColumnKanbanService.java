package pe.edu.utp.prisma_api.domain.columnKanban.services;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;

public interface ColumnKanbanService {
    List<ColumnKanbanDTO> findAllByKanban(String kanbanId);

    Optional<ColumnKanbanDTO> findById(String id);

    ColumnKanbanDTO save(String kanbanId, CreateColumnKanbanDTO dto);

    Optional<ColumnKanbanDTO> update(String id, UpdateColumnKanbanDTO dto);

    void delete(String id);

    void reorder(String kanbanId, List<String> columnIds);
}