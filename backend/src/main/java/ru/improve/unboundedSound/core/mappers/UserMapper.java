package ru.improve.unboundedSound.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.improve.unboundedSound.api.dto.user.UserDataResponse;
import ru.improve.unboundedSound.api.dto.user.UserPostRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostResponse;
import ru.improve.unboundedSound.core.models.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userPostRequestToUser(UserPostRequest userPostRequest);

    UserPostResponse userToUserPostResponse(User user);

    UserDataResponse userToUserGetProfileResponse(User user);
}
