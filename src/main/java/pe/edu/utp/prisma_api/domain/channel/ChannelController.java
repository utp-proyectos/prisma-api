package pe.edu.utp.prisma_api.domain.channel;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.ChannelDetailResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.ChannelResponse;

@RestController
@RequestMapping("/api/projects/{projectId}/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ChannelResponse>>> getChannels(@PathVariable UUID projectId) {
    return ResponseEntity.ok(ApiResponse.ok(channelService.findAllByProjectId(projectId)));
  }

  @GetMapping("/{channelId}")
  public ResponseEntity<ApiResponse<ChannelDetailResponse>> getChannel(@PathVariable UUID channelId) {
    return ResponseEntity.ok(ApiResponse.ok(channelService.findById(channelId)));
  }

}
