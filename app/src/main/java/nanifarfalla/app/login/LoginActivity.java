package nanifarfalla.app.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nanifarfalla.app.R;
import nanifarfalla.app.di.DependencyProvider;
import nanifarfalla.app.login.presentation.LoginFragment;
import nanifarfalla.app.login.presentation.LoginPresenter;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // <<create>> LoginFragment
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_login_container);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_login_container, loginFragment)
                    .commit();
        }

        // <<create>> LoginPresenter
        LoginPresenter loginPresenter = new LoginPresenter(loginFragment,
                DependencyProvider.provideLoginInteractor(this));
    }
}
