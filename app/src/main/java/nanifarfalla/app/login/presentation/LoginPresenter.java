package nanifarfalla.app.login.presentation;

import com.google.common.base.Preconditions;
import nanifarfalla.app.login.domain.usecases.ILoginInteractor;

/**
 * Presentador de la UI del Login
 */

public class LoginPresenter implements
        LoginMvp.Presenter,
        ILoginInteractor.OnLoginFinishedListener {

    // Relaciones de composición
    private final LoginMvp.View mLoginView;
    private final ILoginInteractor mLoginInteractor;

    public LoginPresenter(LoginMvp.View loginView, ILoginInteractor loginInteractor) {
        mLoginView = Preconditions.checkNotNull(loginView);
        mLoginInteractor = Preconditions.checkNotNull(loginInteractor);
        mLoginView.setPresenter(this);
    }

    @Override
    public void validateCredentials(String email, String password) {
        mLoginView.showLoadingIndicator(true);
        mLoginInteractor.login(email, password, this);
    }

    @Override
    public void onEmailError() {
        mLoginView.showLoadingIndicator(false);
        mLoginView.showEmailError();
    }

    @Override
    public void onPasswordError() {
        mLoginView.showLoadingIndicator(false);
        mLoginView.showPasswordError();
    }

    @Override
    public void onError(String error) {
        mLoginView.showLoadingIndicator(false);
        mLoginView.showLoginError(error);
    }

    @Override
    public void onSuccess() {
        mLoginView.showLoadingIndicator(false);
        mLoginView.showInvoicesScreen();
    }
}
