package nanifarfalla.app.products.domain.usecases;

import android.support.annotation.NonNull;

import com.hermosaprogramacion.premium.appproductos.selection.Query;
import com.hermosaprogramacion.premium.appproductos.products.domain.model.Product;

import java.util.List;

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
