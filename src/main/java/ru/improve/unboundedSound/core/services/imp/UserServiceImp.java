package ru.improve.unboundedSound.core.services.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.improve.unboundedSound.api.dto.user.UserDataResponse;
import ru.improve.unboundedSound.api.dto.user.UserPostRequest;
import ru.improve.unboundedSound.api.dto.user.UserPostResponse;
import ru.improve.unboundedSound.api.error.OpenfyException;
import ru.improve.unboundedSound.core.mappers.UserMapper;
import ru.improve.unboundedSound.core.models.User;
import ru.improve.unboundedSound.core.repositories.UserRepository;
import ru.improve.unboundedSound.core.services.UserService;

import static ru.improve.unboundedSound.api.error.ErrorCode.ALREADY_EXIST;
import static ru.improve.unboundedSound.api.error.ErrorCode.UNAUTHORIZED;

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
            throw new OpenfyException(ALREADY_EXIST, new String[]{"email"});
        }

        return userMapper.userToUserPostResponse(user);
    }

    @Transactional()
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new OpenfyException(UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
    }
}
