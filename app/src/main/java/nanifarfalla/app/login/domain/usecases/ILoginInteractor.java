package nanifarfalla.app.login.domain.usecases;

/**
 * Definición general del interactor de login
 */

public interface ILoginInteractor {

    void login(String email, String password, OnLoginFinishedListener callback);

    interface OnLoginFinishedListener {
        void onEmailError();
        void onPasswordError();
        void onError(String error);
        void onSuccess();
    }
}
