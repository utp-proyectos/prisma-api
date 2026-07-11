package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnOrderDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ReorderColumnsDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ColumnKanbanService {

    private final ColumnKanbanRepository columnRepository;
    private final KanbanRepository kanbanRepository;
    private final ColumnKanbanMapper mapper;

    public void createDefaultColumns(Kanban kanban) {

        ColumnKanban pending = new ColumnKanban();
        pending.setTitle("Pendiente");
        pending.setType(ColumnType.PENDING);
        pending.setPosition(0);
        pending.setFixed(false);
        pending.setKanban(kanban);

        ColumnKanban progress = new ColumnKanban();
        progress.setTitle("En curso");
        progress.setType(ColumnType.IN_PROGRESS);
        progress.setPosition(1);
        progress.setFixed(false);
        progress.setKanban(kanban);

        ColumnKanban completed = new ColumnKanban();
        completed.setTitle("Completado");
        completed.setType(ColumnType.COMPLETED);
        completed.setPosition(2);
        completed.setFixed(true);
        completed.setKanban(kanban);

        kanban.getColumns().addAll(List.of(
                pending,
                progress,
                completed));
    }

    @Transactional(readOnly = true)
    public List<ColumnKanbanDetailResponse> findAllByKanban(UUID kanbanId) {

        return columnRepository.findAllByKanbanWithTasks(kanbanId)
                .stream()
                .map(mapper::toDetail)
                .toList();
    }

    public Optional<ColumnKanbanDetailResponse> findById(UUID id) {
        return columnRepository.findById(id)
                .map(mapper::toDetail);
    }

    public ColumnKanbanDetailResponse save(UUID kanbanId, CreateColumnKanbanDTO dto) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        int nextPosition = columnRepository.findLastPosition(kanbanId) + 1;

        ColumnKanban column = mapper.toEntity(dto);

        column.setKanban(kanban);
        column.setPosition(nextPosition);
        column.setFixed(false);
        column.setType(ColumnType.CUSTOM);

        columnRepository.save(column);

        return mapper.toDetail(column);
    }

    public Optional<ColumnKanbanDetailResponse> update(UUID id, UpdateColumnKanbanDTO dto) {
        return columnRepository.findWithKanbanAndProjectAndTeamById(id)
                .map(existing -> {

                    mapper.update(dto, existing);
                    ColumnKanban guardado = columnRepository.save(existing);
                    return mapper.toDetail(guardado);
                });
    }

    public void delete(UUID id) {
        ColumnKanban column = columnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrada"));

        if (column.isFixed()) {
            throw new IllegalArgumentException("No se pueden eliminar columnas fijas");
        }

        columnRepository.delete(column);
    }

    public void reorderColumns(ReorderColumnsDTO dto) {
        for (ColumnOrderDTO colDto : dto.getColumns()) {
            columnRepository.updatePosition(colDto.getId(), colDto.getPosition());
        }
    }
}