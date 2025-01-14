package ru.improve.openfy.core.services.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.improve.openfy.api.dto.user.UserDataResponse;
import ru.improve.openfy.api.dto.user.UserPostRequest;
import ru.improve.openfy.api.dto.user.UserPostResponse;
import ru.improve.openfy.api.error.ServiceException;
import ru.improve.openfy.core.mappers.UserMapper;
import ru.improve.openfy.core.models.User;
import ru.improve.openfy.core.repositories.UserRepository;
import ru.improve.openfy.core.services.UserService;

import static ru.improve.openfy.api.error.ErrorCode.ALREADY_EXIST;
import static ru.improve.openfy.api.error.ErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Transactional
    @Override
    public UserDataResponse getUserProfile() {
        User user = getCurrentUser();
        return userMapper.userToUserGetProfileResponse(user);
    }

    @Transactional
    @Override
    public UserPostResponse createNewUser(UserPostRequest userPostRequest) {
        User user = userMapper.userPostRequestToUser(userPostRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ServiceException(ALREADY_EXIST, new String[]{"email"});
        }

        return userMapper.userToUserPostResponse(user);
    }

    @Transactional()
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ServiceException(UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
    }
}
