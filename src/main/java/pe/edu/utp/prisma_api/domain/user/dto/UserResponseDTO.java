package pe.edu.utp.prisma_api.domain.user.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private boolean emailVerified;
}