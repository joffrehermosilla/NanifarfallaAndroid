package nanifarfalla.app.data.api;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Segmentos de URL donde actuaremos
 */
public interface RestService {
    @GET("products")
    Call<List<Product>> getProducts();
}
