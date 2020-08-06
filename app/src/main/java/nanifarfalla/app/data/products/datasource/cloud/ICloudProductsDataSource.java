package nanifarfalla.app.data.products.datasource.cloud;

import java.util.List;

import nanifarfalla.app.products.domain.criteria.ProductCriteria;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Interfaz de comunicación con el repositorio para la fuente de datos remota
 */
public interface ICloudProductsDataSource {

    interface ProductServiceCallback {

        void onLoaded(List<Product> products);

        void onError(String error);

    }

    void getProducts(ProductServiceCallback callback, ProductCriteria criteria);

}
