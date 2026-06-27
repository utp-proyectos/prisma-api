package pe.edu.utp.prisma_api.domain.team;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.project.ProjectMapper;
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.UpdateTeamDTO;
import pe.edu.utp.prisma_api.domain.user.UserMapper;

@Mapper(componentModel = "spring", uses = { ProjectMapper.class, UserMapper.class })
public interface TeamMapper {

    TeamDTO toDto(Team team);

    List<TeamDTO> toDto(List<Team> teams);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Team toEntity(CreateTeamDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "projects", ignore = true)
    void update(UpdateTeamDTO dto,
            @MappingTarget Team team);
}
