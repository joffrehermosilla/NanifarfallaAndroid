package nanifarfalla.app.login.data;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.common.base.Preconditions;
import nanifarfalla.app.R;
import nanifarfalla.app.login.data.cloud.ICloudUsersDataSource;
import nanifarfalla.app.login.data.preferences.IUserPreferences;
import nanifarfalla.app.login.domain.entities.User;

/**
 * Implementación concreta del repositorio de usuarios
 */

public class UsersRepository implements IUsersRepository {

    private static UsersRepository INSTANCE = null;

    // Relaciones de composición
    private final ICloudUsersDataSource mUserService;
    private final IUserPreferences mUserPreferences;

    private final ConnectivityManager mConnectivityManager;
    private final Resources mResources;


    private UsersRepository(ICloudUsersDataSource userService,
                            IUserPreferences userPreferences,
                            Context context) {
        mUserService = Preconditions.checkNotNull(userService);
        mUserPreferences = Preconditions.checkNotNull(userPreferences);
        mConnectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mResources = context.getResources();
    }

    public static UsersRepository getInstance(ICloudUsersDataSource userService,
                                              IUserPreferences userPreferences,
                                              Context controllerContext) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(userService, userPreferences, controllerContext);
        }
        return INSTANCE;
    }

    @Override
    public void auth(String email, String password, final OnAuthenticateListener callback) {
        if (!isNetworkAvailable()) {
            callback.onError(mResources.getString(R.string.error_network));
            return;
        }

        mUserService.auth(email, password,
                new ICloudUsersDataSource.UserServiceCallback() {
                    @Override
                    public void onAuthFinished(User user) {
                        mUserPreferences.save(user);
                        callback.onSuccess();
                    }

                    @Override
                    public void onAuthFailed(String error) {
                        callback.onError(error);
                    }
                });
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm = mConnectivityManager;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void closeSession() {
        mUserPreferences.delete();
    }

}
