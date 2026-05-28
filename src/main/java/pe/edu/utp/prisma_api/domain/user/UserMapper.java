package pe.edu.utp.prisma_api.domain.user;

import org.mapstruct.Mapper;

import pe.edu.utp.prisma_api.domain.user.dto.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTO(User user);
}
