package nanifarfalla.app.products.data;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

/**
 * Repositorio de productos
 */
public interface IProductsRepository {
    interface GetProductsCallback {

        void onProductsLoaded(List<Product> products);

        void onDataNotAvailable(String error);
    }

    void getProducts(@NonNull Query query, GetProductsCallback callback);

    void refreshProducts();
}
