package ru.improve.openfy.core.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.improve.openfy.api.error.ServiceException;
import ru.improve.openfy.core.repositories.UserRepository;
import ru.improve.openfy.util.MessageKeys;

import static ru.improve.openfy.api.error.ErrorCode.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(login).orElseThrow(
                () -> new ServiceException(NOT_FOUND, MessageKeys.TITLE_USER_NOT_FOUND)
        );
    }
}
