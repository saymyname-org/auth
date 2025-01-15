package ru.improve.openfy.core.services;

import ru.improve.openfy.core.models.Session;
import ru.improve.openfy.core.models.User;

public interface SessionService {

    Session create(User user);

    boolean checkSessionEnableById(long id);

    boolean checkSessionEnable(Session session);

    void setUserSessionDisable(User user);
}
