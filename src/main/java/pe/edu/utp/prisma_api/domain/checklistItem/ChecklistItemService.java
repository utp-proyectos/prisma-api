package pe.edu.utp.prisma_api.domain.checklistItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.checklist.Checklist;
import pe.edu.utp.prisma_api.domain.checklist.ChecklistRepository;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.UpdateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.CreateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.ChecklistItemResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemMapper checklistItemMapper;

    public List<ChecklistItemResponse> findAllByChecklistId(UUID checklistId) {
        return checklistItemMapper.toDto(checklistItemRepository.findAllByChecklistId(checklistId));
    }

    public Optional<ChecklistItemResponse> findById(UUID id) {
        return checklistItemRepository.findById(id).map(checklistItemMapper::toDto);
    }

    public ChecklistItemResponse save(UUID checklistId, CreateChecklistItemDTO dto) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist no encontrada"));

        ChecklistItem checklistItem = checklistItemMapper.toEntity(dto);
        checklistItem.setChecklist(checklist);

        checklistItemRepository.save(checklistItem);
        return checklistItemMapper.toDto(checklistItem);
    }

    public ChecklistItemResponse update(UUID id, UpdateChecklistItemDTO dto) {
        ChecklistItem checklistItem = checklistItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem no encontrada"));

        checklistItemMapper.update(dto, checklistItem);
        checklistItemRepository.save(checklistItem);
        return checklistItemMapper.toDto(checklistItem);
    }

    public ChecklistItemResponse delete(UUID id) {
        ChecklistItem checklistItem = checklistItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChecklistItem no encontrada"));

        ChecklistItemResponse response = checklistItemMapper.toDto(checklistItem);
        checklistItemRepository.delete(checklistItem);

        return response;
    }
}