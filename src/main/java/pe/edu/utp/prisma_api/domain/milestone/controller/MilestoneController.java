package pe.edu.utp.prisma_api.domain.milestone.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneService;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;

@RestController
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping("/api/kanbans/{kanbanId}/milestones")
    public ResponseEntity<ApiResponse<List<MilestoneSummaryResponse>>> findAll(
            @PathVariable UUID kanbanId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        milestoneService.findAllByKanbanId(kanbanId)));
    }

    @GetMapping("/api/milestones/{milestoneId}")
    public ResponseEntity<ApiResponse<MilestoneDetailResponse>> findById(
            @PathVariable UUID milestoneId) {

        MilestoneDetailResponse milestone = milestoneService.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone no encontrado"));

        return ResponseEntity.ok(
                ApiResponse.ok(
                        milestone));
    }

}
