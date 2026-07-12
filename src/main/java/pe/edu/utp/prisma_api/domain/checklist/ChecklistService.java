package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDetailResponse;
import pe.edu.utp.prisma_api.domain.checklist.dto.CreateChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.UpdateChecklistDTO;
import pe.edu.utp.prisma_api.domain.task.Task;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;
    private final ChecklistMapper checklistMapper;

    public List<ChecklistDetailResponse> findAllByTaskId(UUID taskId) {
        return checklistMapper.toDto(checklistRepository.findAllByTaskId(taskId));
    }

    public Optional<ChecklistDetailResponse> findById(UUID id) {
        return checklistRepository.findById(id)
                .map(checklistMapper::toDto);
    }

    public ChecklistDetailResponse save(UUID taskId, CreateChecklistDTO dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrado"));

        Checklist checklist = checklistMapper.toEntity(dto);
        checklist.setTask(task);
        checklistRepository.save(checklist);

        return checklistMapper.toDto(checklist);
    }

    public ChecklistDetailResponse update(UUID id, UpdateChecklistDTO dto) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist no encontrado"));

        checklistMapper.update(dto, checklist);

        return checklistMapper.toDto(checklist);
    }

    public void delete(UUID id) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist no encontrado"));

        checklistRepository.delete(checklist);
    }

}
