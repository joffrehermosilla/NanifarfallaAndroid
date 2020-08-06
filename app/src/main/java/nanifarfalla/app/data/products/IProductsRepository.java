package nanifarfalla.app.data.products;

import java.util.List;

import nanifarfalla.app.products.domain.criteria.ProductCriteria;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Repositorio de productos
 */
public interface IProductsRepository {
    interface GetProductsCallback {

        void onProductsLoaded(List<Product> products);

        void onDataNotAvailable(String error);
    }

    void getProducts(GetProductsCallback callback, ProductCriteria criteria);

    void refreshProducts();
}
