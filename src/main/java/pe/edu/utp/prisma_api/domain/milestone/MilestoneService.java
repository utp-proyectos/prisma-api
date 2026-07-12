package pe.edu.utp.prisma_api.domain.milestone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;
import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.UpdateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;
    private final KanbanRepository kanbanRepository;
    private final TaskRepository taskRepository;

    public List<MilestoneSummaryResponse> findAllByKanbanId(UUID kanbanId) {
        return milestoneMapper.toDto(milestoneRepository.findAllByKanbanId(kanbanId));
    }

    public Optional<MilestoneDetailResponse> findById(UUID id) {
        return milestoneRepository.findById(id).map(milestoneMapper::toDetail);
    }

    public Optional<MilestoneSummaryResponse> findSummaryById(UUID id) {
        return milestoneRepository.findById(id).map(milestoneMapper::toDto);
    }

    public MilestoneSummaryResponse save(UUID kanbanId, CreateMilestoneDTO dto) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        Milestone milestone = milestoneMapper.toEntity(dto);
        milestone.setKanban(kanban);

        milestone = milestoneRepository.save(milestone);

        return milestoneMapper.toDto(milestone);
    }

    public Optional<MilestoneSummaryResponse> update(UUID id, UpdateMilestoneDTO dto) {
        return milestoneRepository.findWithAllDetailsById(id)
                .map(existing -> {

                    milestoneMapper.update(dto, existing);
                    Milestone guardado = milestoneRepository.save(existing);
                    return milestoneMapper.toDto(guardado);
                });
    }

    public void delete(UUID id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hito no encontrado"));

        taskRepository.disassociateTasksFromMilestone(id);

        milestoneRepository.delete(milestone);
    }

    public List<MilestoneSummaryResponse> refresh(UUID kanbanId) {
        return milestoneMapper.toDto(
                milestoneRepository.findAllByKanbanId(kanbanId));
    }

    public List<MilestoneDetailResponse> refreshDetails(UUID kanbanId) {
        return milestoneRepository.findAllByKanbanId(kanbanId)
                .stream()
                .map(milestoneMapper::toDetail)
                .toList();
    }
}
