package nanifarfalla.app.login.data.preferences;

import nanifarfalla.app.login.domain.entities.User;

/**
 * Representación de las preferencias de usuarios
 */

public interface IUserPreferences {

    void save(User user);

    void delete();

    boolean isLoggedIn();

    String getAccessToken();

    String getUserName();

    String getUserId();
}
