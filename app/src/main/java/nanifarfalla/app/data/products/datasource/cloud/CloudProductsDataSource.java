package nanifarfalla.app.data.products.datasource.cloud;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import nanifarfalla.app.data.api.ErrorResponse;
import nanifarfalla.app.data.api.RestService;
import nanifarfalla.app.products.domain.criteria.ProductCriteria;
import nanifarfalla.app.products.domain.model.Product;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fuente de datos relacionada al servidor remoto
 */
public class CloudProductsDataSource implements ICloudProductsDataSource {

    // Si vas a usar un dominio real o la ip de tu pc, cambia los valores de las
    // constantes o tendrás errores de ejecución.
    public static final String BASE_URL_REAL_DOMAIN
            = "http://<PON-AQUI-TU-DOMINIO-O-IP-PUBLICA>/api.appproducts.com/v1/";
    public static final String BASE_URL_REAL_DEVICE =
            "http://<PON-AQUI-TU-IP-LOCAL/api.appproducts.com/v1/";
    public static final String BASE_URL_AVD = "http://10.0.2.2/api.appproducts.com/v1/";
    public static final String BASE_URL_GENYMOTION = "http://10.0.3.2/api.appproducts.com/v1/";

    private final Retrofit mRetrofit;
    private final RestService mRestService;

    public CloudProductsDataSource() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_AVD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRestService = mRetrofit.create( RestService.class);
    }


    @Override
    public void getProducts(final ProductServiceCallback callback,
                            ProductCriteria criteria) {

        Call<List<Product>> call = mRestService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call,
                                   Response<List<Product>> response) {
                // Procesamos los posibles casos
                processGetProductsResponse(response, callback);

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }





    private void processGetProductsResponse(Response<List<Product>> response,
                                            ProductServiceCallback callback) {
        String error = "Ha ocurrido un error";
        ResponseBody errorBody = response.errorBody();

        // ¿Hubo un error?
        if (errorBody != null) {
            // ¿Fué de la API?
            if (errorBody.contentType().subtype().equals("json")) {
                try {
                    // Parseamos el objeto
                    ErrorResponse er = new Gson()
                            .fromJson(errorBody.string(), ErrorResponse.class);
                    error = er.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            callback.onError(error);
        }

        // ¿LLegaron los productos sanos y salvos?
        if (response.isSuccessful()) {
            callback.onLoaded(response.body());
        }


    }
}
