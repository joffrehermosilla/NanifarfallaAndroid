package nanifarfalla.app.login.data.cloud;

import android.util.Base64;

import nanifarfalla.app.external.api.ErrorResponse;
import nanifarfalla.app.external.api.RestService;
import nanifarfalla.app.login.domain.entities.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementación concreta de la fuente de datos remota
 */

public class CloudUsersDataSource implements ICloudUsersDataSource {

    private static CloudUsersDataSource INSTANCE;

    private Retrofit mRestAdapter;
    private RestService mRestClient;

    private CloudUsersDataSource() {
        mRestAdapter = new Retrofit.Builder().
                baseUrl(RestService.APP_PRODUCTOS_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestClient = mRestAdapter.create(RestService.class);
    }

    public static CloudUsersDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CloudUsersDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void auth(final String email, final String password,
                     final ICloudUsersDataSource.UserServiceCallback callback) {

        // Encodificar credenciales en Base64
        String formatCredentials = String.format("%s:%s", email, password);
        String base64Credentials = Base64.encodeToString(
                formatCredentials.getBytes(), Base64.NO_WRAP);

        // Preparar llamada de login
        Call<User> userCall = mRestClient.login("Basic " + base64Credentials);

        // Realizar petición POST a /sessions
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onAuthFinished(response.body());
                    return;
                }

                ResponseBody errorBody = response.errorBody();

                if (errorBody.contentType().subtype().equals("json")) {
                    ErrorResponse errorResponse = ErrorResponse.fromErrorBody(errorBody);
                    callback.onAuthFailed(errorResponse.getMessage());
                }

                callback.onAuthFailed(response.code() + " " + response.message());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onAuthFailed(t.getMessage());
            }
        });
    }
}
