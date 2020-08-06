package nanifarfalla.app.data.products.datasource.memory;

import java.util.List;

import nanifarfalla.app.products.domain.criteria.ProductCriteria;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Interfaz para fuente de datos en memoria
 */
public interface IMemoryProductsDataSource {
    List<Product> find(ProductCriteria criteria);

    void save(Product product);

    void deleteAll();

    boolean mapIsNull();
}
