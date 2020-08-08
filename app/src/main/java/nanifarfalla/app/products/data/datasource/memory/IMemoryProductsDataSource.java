package nanifarfalla.app.products.data.datasource.memory;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

/**
 * Interfaz para fuente de datos en memoria
 */
public interface IMemoryProductsDataSource {
    List<Product> find(@NonNull Query query);

    void save(Product product);

    void deleteAll();

    boolean mapIsNull();
}
