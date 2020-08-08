package nanifarfalla.app.products.data.datasource.cloud;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

/**
 * Interfaz de comunicación con el repositorio para la fuente de datos remota
 */
public interface ICloudProductsDataSource {

    interface ProductServiceCallback {

        void onLoaded(List<Product> products);

        void onError(String error);

    }

    void getProducts(@NonNull Query query, String userToken, ProductServiceCallback callback);

}
