package ru.improve.openfy.core.services.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.improve.openfy.api.error.ServiceException;
import ru.improve.openfy.configuration.security.SessionConfiguration;
import ru.improve.openfy.core.models.Session;
import ru.improve.openfy.core.models.User;
import ru.improve.openfy.core.repositories.SessionRepository;
import ru.improve.openfy.core.services.SessionService;

import java.time.Instant;

import static ru.improve.openfy.api.error.ErrorCode.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class SessionServiceImp implements SessionService {

    private final SessionRepository sessionRepository;

    private final SessionConfiguration sessionConfiguration;

    @Transactional
    @Override
    public Session create(User user) {
        setUserSessionDisable(user);
        Session session = Session.builder()
                .expiredAt(Instant.now().plus(sessionConfiguration.getDuration()))
                .user(user)
                .build();
        sessionRepository.save(session);
        return session;
    }

    @Transactional
    @Override
    public boolean checkSessionEnable(long id) {
        Session session = sessionRepository.findById(id).orElseThrow(
                () -> new ServiceException(NOT_FOUND));

        Instant nowTime = Instant.now();
        if (nowTime.isAfter(session.getExpiredAt())) {
            session.setEnable(false);
        }
        return session.isEnable();
    }

    @Transactional
    @Override
    public void setUserSessionDisable(User user) {
        sessionRepository.findByUserAndIsEnable(user, true)
                .ifPresent(session -> {
                    session.setEnable(false);
                });
    }
}
