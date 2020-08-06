package nanifarfalla.app.login.data.preferences;

import com.hermosaprogramacion.premium.appproductos.login.domain.entities.User;

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
