package pe.edu.utp.prisma_api.domain.milestone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneService;
import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.DeleteMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.UpdateMilestoneDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class MilestoneWsController {

        private final MilestoneService milestoneService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/milestone.create")
        public void create(
                        @Payload @Valid CreateMilestoneDTO dto) {

                MilestoneSummaryResponse milestone = milestoneService.save(dto.getKanbanId(), dto);

                String topic = "/topic/" +
                                milestone.getTeamId() + "/" +
                                milestone.getProjectId() + "/" +
                                milestone.getKanbanId() + "/milestones";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                milestone));
        }

        @MessageMapping("/milestone.update")
        public void updateMilestone(@Valid @Payload UpdateMilestoneDTO dto) {

                MilestoneSummaryResponse milestoneActualizado = milestoneService.update(dto.getMilestoneId(), dto)
                                .orElseThrow(() -> new ResourceNotFoundException("Milestone no encontrado"));

                String topic = "/topic/" +
                                milestoneActualizado.getTeamId() + "/" +
                                milestoneActualizado.getProjectId() + "/" +
                                milestoneActualizado.getKanbanId() + "/milestones";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.UPDATE,
                                                milestoneActualizado));
        }

        @MessageMapping("/milestone.delete")
        public void deleteMilestone(@Payload DeleteMilestoneDTO dto) {

                MilestoneDetailResponse milestone = milestoneService.findById(dto.getMilestoneId())
                                .orElseThrow(() -> new ResourceNotFoundException("Milestone no encontrado"));

                milestoneService.delete(dto.getMilestoneId());

                String topic = "/topic/" +
                                milestone.getTeamId() + "/" +
                                milestone.getProjectId() + "/" +
                                milestone.getKanbanId() + "/milestones";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.DELETE,
                                                milestone));
        }

}