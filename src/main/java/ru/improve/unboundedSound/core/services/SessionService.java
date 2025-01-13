package ru.improve.unboundedSound.core.services;

import ru.improve.unboundedSound.core.models.Session;
import ru.improve.unboundedSound.core.models.User;

public interface SessionService {

    Session create(User user);

    boolean checkSessionEnable(long id);

    void setUserSessionDisable(User user);
}
