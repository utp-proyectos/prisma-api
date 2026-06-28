package pe.edu.utp.prisma_api.domain.columnKanban.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanban;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanMapper;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanRepository;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;

@Service
@RequiredArgsConstructor
public class ColumnKanbanServiceImpl implements ColumnKanbanService {

    private final ColumnKanbanRepository columnRepository;
    private final KanbanRepository kanbanRepository;
    private final ColumnKanbanMapper mapper;

    @Override
    public List<ColumnKanbanDTO> findAllByKanban(UUID kanbanId) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        return kanban.getColumns()
                .stream()
                .sorted(Comparator.comparing(ColumnKanban::getPosition))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public Optional<ColumnKanbanDTO> findById(UUID id) {
        return columnRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public ColumnKanbanDTO save(UUID kanbanId, CreateColumnKanbanDTO dto) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        ColumnKanban column = mapper.toEntity(dto);
        column.setKanban(kanban);
        column.setPosition(kanban.getColumns().size());
        kanban.getColumns().add(column);

        kanbanRepository.save(kanban);

        return mapper.toDto(column);
    }

    @Override
    public Optional<ColumnKanbanDTO> update(UUID id, UpdateColumnKanbanDTO dto) {

        return columnRepository.findById(id)
                .map(column -> {
                    mapper.update(dto, column);

                    return mapper.toDto(columnRepository.save(column));
                });
    }

    @Override
    public void delete(UUID id) {

        ColumnKanban column = columnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrado"));

        Kanban kanban = column.getKanban();
        kanban.getColumns().remove(column);

        kanbanRepository.save(kanban);
    }

    @Override
    public void reorder(UUID kanbanId, List<UUID> columnIds) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        Map<UUID, ColumnKanban> map = kanban.getColumns()
                .stream()
                .collect(Collectors.toMap(ColumnKanban::getId, Function.identity()));

        for (int i = 0; i < columnIds.size(); i++) {

            ColumnKanban column = map.get(columnIds.get(i));

            if (column != null) {
                column.setPosition(i);
            }
        }

        columnRepository.saveAll(kanban.getColumns());
    }
}