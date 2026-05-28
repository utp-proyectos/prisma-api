package pe.edu.utp.prisma_api.domain.team;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.team.dto.TeamRequestDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamResponseDTO;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    // Para listados simples
    TeamResponseDTO toResponseDTO(Team team);

    // Para crear un nuevo equipo
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Team toEntity(TeamRequestDTO dto);

    // Para actualizar un equipo existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void updateEntityFromDto(TeamRequestDTO dto, @MappingTarget Team team);
}
