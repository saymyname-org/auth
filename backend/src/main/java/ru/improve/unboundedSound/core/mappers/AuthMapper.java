package ru.improve.unboundedSound.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.improve.unboundedSound.api.dto.auth.LoginResponse;
import ru.improve.unboundedSound.core.models.Session;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    LoginResponse sessionToLoginResponse(Session session);
}
