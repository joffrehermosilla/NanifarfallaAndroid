package nanifarfalla.app.login.presentation;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Preconditions;

import nanifarfalla.app.R;
import nanifarfalla.app.invoices.InvoicesActivity;

//import android.support.v4.app.Fragment;

/**
 * Fragmento del login que actúa como immplementación concreta de la vista
 */
public class LoginFragment extends Fragment implements LoginMvp.View {

    // Miembros UI
    private ImageView mImageLogo;
    private EditText mEmailField;
    private TextInputLayout mEmailFloatingLabel;
    private EditText mPasswordField;
    private TextInputLayout mPasswordFloatingLabel;
    private Button mButtonLogin;
    private ProgressBar mProgressLogin;

    // Relación de composición
    private LoginMvp.Presenter mLoginPresenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View uiRoot = inflater.inflate(R.layout.fragment_login, container, false);

        // UI
        mImageLogo = (ImageView) uiRoot.findViewById(R.id.image_logo);
        mEmailField = (EditText) uiRoot.findViewById(R.id.text_field_email);
        mEmailFloatingLabel = (TextInputLayout) uiRoot.findViewById(R.id.float_label_email);
        mPasswordField = (EditText) uiRoot.findViewById(R.id.text_field_password);
        mPasswordFloatingLabel = (TextInputLayout) uiRoot.findViewById(R.id.float_label_password);
        mButtonLogin = (Button) uiRoot.findViewById(R.id.button_login);
        mProgressLogin = (ProgressBar) uiRoot.findViewById(R.id.progress_login);

        // Setup UI
        Glide.with(this).load("file:///android_asset/logo.png").into(mImageLogo);
        mEmailField.addTextChangedListener(new TextWatcherLabel(mEmailFloatingLabel));
        mPasswordField.addTextChangedListener(new TextWatcherLabel(mPasswordFloatingLabel));
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(mEmailField.getText().toString(),
                        mPasswordField.getText().toString());
                closeKeyboard();
            }
        });

        return uiRoot;
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
    }

    @Override
    public void showEmailError() {
        mEmailFloatingLabel.setError(getString(R.string.error_login));
    }

    @Override
    public void showPasswordError() {
        mPasswordFloatingLabel.setError(getString(R.string.error_password));
    }

    @Override
    public void login(String email, String password) {
        mLoginPresenter.validateCredentials(email, password);
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        mProgressLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showInvoicesScreen() {
        startActivity(new Intent(getActivity(), InvoicesActivity.class));
        getActivity().finish();
    }

    @Override
    public void setPresenter(LoginMvp.Presenter presenter) {
        mLoginPresenter = Preconditions.checkNotNull(presenter);
    }

    class TextWatcherLabel implements TextWatcher {

        private final TextInputLayout mFloatingLabel;

        public TextWatcherLabel(TextInputLayout floatingLabel) {
            mFloatingLabel = floatingLabel;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mFloatingLabel.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
