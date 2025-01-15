package ru.improve.openfy.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.improve.openfy.core.models.Session;
import ru.improve.openfy.core.models.User;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUserAndIsEnable(User user, boolean enable);

    Optional<Session> findById(long id);
}
