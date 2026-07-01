package pe.edu.utp.prisma_api.domain.milestone.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneService;
import pe.edu.utp.prisma_api.domain.milestone.dto.CreateMilestoneDTO;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class MilestoneWsController {

        private final MilestoneService milestoneService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/milestone.create")
        public void create(
                        @Payload @Valid CreateMilestoneDTO dto) {

                System.out.println(dto.getKanbanId());

                MilestoneSummaryResponse milestone = milestoneService.save(dto.getKanbanId(), dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/milestones";

                System.out.println("Publicando en Redis hacia el tópico: " + topic);

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                milestone));
        }
}