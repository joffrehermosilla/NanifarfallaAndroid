package nanifarfalla.app.products.domain.usecases;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.products.data.IProductsRepository;
import nanifarfalla.app.products.data.ProductsRepository;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación concreta del interactor de detalle de producto
 */

public class GetProducts implements IGetProducts {
    private IProductsRepository mProductsRepository;


    public GetProducts(ProductsRepository productsRepository) {
        mProductsRepository = checkNotNull(productsRepository,
                "productsRepository no puede ser null");
    }

    @Override
    public void getProducts(@NonNull final Query query, boolean forceLoad,
                            final GetProductsCallback callback) {
        checkNotNull(query, "query no puede ser null");
        checkNotNull(callback, "callback no puede ser null");


        if (forceLoad) {
            mProductsRepository.refreshProducts();
        }

        mProductsRepository.getProducts(query, new IProductsRepository.GetProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Product> products) {
                        checkNotNull(products, "products no puede ser null");

                        callback.onSuccess(products);
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        callback.onError(error);
                    }
                }
        );
    }
}
