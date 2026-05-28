package pe.edu.utp.prisma_api.domain.user;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.user.dto.UserResponseDTO;

public interface UserService {
    List<UserResponseDTO> getAll();

    public Optional<UserResponseDTO> get(String userId);
}
