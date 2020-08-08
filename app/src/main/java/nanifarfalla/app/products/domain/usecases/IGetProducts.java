package nanifarfalla.app.products.domain.usecases;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

/**
 * Abstracción del interactor para obtener productos
 */

public interface IGetProducts {
    void getProducts(@NonNull Query query, boolean forceLoad, GetProductsCallback callback);

    interface GetProductsCallback {
        void onSuccess(List<Product> products);

        void onError(String error);
    }
}
