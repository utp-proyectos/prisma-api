package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;

@Service
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

    public List<ColumnKanbanDetailResponse> findAllByKanban(UUID kanbanId) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        return kanban.getColumns()
                .stream()
                .sorted(Comparator.comparing(column -> column.getPosition()))
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

    // public Optional<ColumnKanbanDTO> update(UUID id, UpdateColumnKanbanDTO dto) {

    // return columnRepository.findById(id)
    // .map(column -> {
    // mapper.update(dto, column);

    // return mapper.toDto(columnRepository.save(column));
    // });
    // }

    // public void delete(UUID id) {

    // ColumnKanban column = columnRepository.findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrado"));

    // Kanban kanban = column.getKanban();
    // kanban.getColumns().remove(column);

    // kanbanRepository.save(kanban);
    // }

    // public void reorder(UUID kanbanId, List<UUID> columnIds) {

    // Kanban kanban = kanbanRepository.findById(kanbanId)
    // .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

    // Map<UUID, ColumnKanban> map = kanban.getColumns()
    // .stream()
    // .collect(Collectors.toMap(
    // column -> column.getId(),
    // column -> column,
    // (existing, replacement) -> existing));

    // for (int i = 0; i < columnIds.size(); i++) {

    // ColumnKanban column = map.get(columnIds.get(i));

    // if (column != null) {
    // column.setPosition(i);
    // }
    // }

    // columnRepository.saveAll(kanban.getColumns());
    // }
}