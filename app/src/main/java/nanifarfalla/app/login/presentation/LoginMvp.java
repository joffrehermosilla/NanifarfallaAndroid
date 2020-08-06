package nanifarfalla.app.login.presentation;

/**
 * Contrato Mvp para Login
 */

public interface LoginMvp {
    interface View {
        void showEmailError();

        void showPasswordError();

        void login(String email, String password);

        void showLoginError(String error);

        void showLoadingIndicator(boolean show);

        void showInvoicesScreen();

        void setPresenter(Presenter presenter);
    }

    interface Presenter{
        void validateCredentials(String email, String password);
    }

}
