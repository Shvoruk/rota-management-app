package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.TeamDTO;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mappings({
            @Mapping(source = "id", target = "teamId"),
            @Mapping(source = "name", target = "teamName")
    })
    TeamDTO toTeamDTO(Team team);

}
