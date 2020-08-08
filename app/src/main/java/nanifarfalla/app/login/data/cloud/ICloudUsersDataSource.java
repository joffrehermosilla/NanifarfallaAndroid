package nanifarfalla.app.login.data.cloud;

import nanifarfalla.app.login.domain.entities.User;

/**
 * Representación de dependencia entre el interactor de login y el controlador de la API
 */

public interface ICloudUsersDataSource {
    void auth(String name, String password, UserServiceCallback callback);


    interface UserServiceCallback {
        void onAuthFinished(User user);
        void onAuthFailed(String error);
    }
}
