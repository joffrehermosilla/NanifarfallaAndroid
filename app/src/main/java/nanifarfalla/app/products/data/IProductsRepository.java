package nanifarfalla.app.products.data;

import android.support.annotation.NonNull;

import nanifarfalla.app.selection.Query;
import nanifarfalla.app.products.domain.model.Product;

import java.util.List;

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
