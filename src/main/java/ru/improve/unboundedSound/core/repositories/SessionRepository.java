package ru.improve.unboundedSound.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.improve.unboundedSound.core.models.Session;
import ru.improve.unboundedSound.core.models.User;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUserAndIsEnable(User user, boolean enable);
}
