package pe.edu.utp.prisma_api.domain.channel;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.channel.dto.ChannelDetailResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.ChannelResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.CreateChannelRequest;
import pe.edu.utp.prisma_api.domain.message.MessageRepository;
import pe.edu.utp.prisma_api.domain.message.dto.MessageResponse;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ChannelService {
  private final ChannelRepository channelRepository;
  private final ProjectRepository projectRepository;
  private final MessageRepository messageRepository;

  public List<ChannelResponse> findAllByProjectId(UUID projectId) {
    return channelRepository.findByProjectId(projectId).stream()
        .map(this::toDto).toList();
  }

  public ChannelDetailResponse findById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado"));

    List<MessageResponse> messages = messageRepository.findByChannelId(channelId).stream()
        .map(message -> new MessageResponse(message.getId(), message.getContent(), message.getSender().getUsername(),
            message.getSender().getAvatar(), message.getSender().getId(), message.getCreatedAt()))
        .toList();

    return new ChannelDetailResponse(channel.getId(), channel.getName(), messages);
  }

  public ChannelResponse save(CreateChannelRequest request) {
    Project project = projectRepository.findById(request.projectId())
        .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

    Channel channel = new Channel();

    channel.setName(request.name());
    channel.setProject(project);

    channelRepository.save(channel);

    return toDto(channel);
  }

  private ChannelResponse toDto(Channel channel) {
    return new ChannelResponse(channel.getId(), channel.getName());
  }
}
