package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(source = "id", target = "teamId")
    TeamDto toTeamDTO(Team team);

    @Mapping(source = "id", target = "teamId")
    Set<TeamDto> toTeamsDto(Set<Team> teams);

}
