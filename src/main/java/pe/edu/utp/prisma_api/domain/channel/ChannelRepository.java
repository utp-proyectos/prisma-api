package pe.edu.utp.prisma_api.domain.channel;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
  List<Channel> findByProjectId(UUID projectId);

}
