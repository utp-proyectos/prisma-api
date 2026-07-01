package pe.edu.utp.prisma_api.domain.milestone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.kanban.Kanban;
import pe.edu.utp.prisma_api.domain.kanban.KanbanRepository;
import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;
    private final KanbanRepository kanbanRepository;

    public List<MilestoneSummaryResponse> findAllByKanbanId(UUID kanbanId) {
        return milestoneMapper.toDto(milestoneRepository.findAllByKanbanId(kanbanId));
    }

    public Optional<MilestoneDetailResponse> findById(UUID id) {
        return milestoneRepository.findById(id).map(milestoneMapper::toDetail);
    }

    public MilestoneSummaryResponse save(UUID kanbanId, CreateMilestoneDTO dto) {

        Kanban kanban = kanbanRepository.findById(kanbanId)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        Milestone milestone = milestoneMapper.toEntity(dto);
        milestone.setKanban(kanban);

        milestone = milestoneRepository.save(milestone);

        return milestoneMapper.toDto(milestone);
    }
}
