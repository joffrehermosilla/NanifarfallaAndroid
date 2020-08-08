package nanifarfalla.app.external.api;

import java.util.List;

import nanifarfalla.app.login.domain.entities.User;
import nanifarfalla.app.products.domain.model.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Segmentos de URL donde actuaremos
 */
public interface RestService {

    // TODO: Usar IP o dominio propios. Agregar esquema "https" si tienes un certificado SSL
    String APP_PRODUCTOS_SERVICE_BASE_URL = "http://104.236.0.232/v1/";

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String authorization);

    @POST("sessions")
    Call<User> login(@Header("Authorization") String authorization);
}
