package ru.improve.unboundedSound.core.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.improve.unboundedSound.api.error.OpenfyException;
import ru.improve.unboundedSound.core.repositories.UserRepository;
import ru.improve.unboundedSound.util.MessageKeys;

import static ru.improve.unboundedSound.api.error.ErrorCode.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(login).orElseThrow(
                () -> new OpenfyException(NOT_FOUND, MessageKeys.TITLE_USER_NOT_FOUND)
        );
    }
}
