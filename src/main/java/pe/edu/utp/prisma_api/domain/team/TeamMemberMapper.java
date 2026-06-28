package pe.edu.utp.prisma_api.domain.team;

import org.mapstruct.Mapper;

import pe.edu.utp.prisma_api.domain.team.dto.TeamMemberDTO;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {
    TeamMemberDTO toDto(TeamMember member);
}
