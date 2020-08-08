package nanifarfalla.app.products.data.datasource.memory;

import android.support.annotation.NonNull;

import nanifarfalla.app.selection.Query;
import nanifarfalla.app.products.domain.model.Product;

import java.util.List;

/**
 * Interfaz para fuente de datos en memoria
 */
public interface IMemoryProductsDataSource {
    List<Product> find(@NonNull Query query);

    void save(Product product);

    void deleteAll();

    boolean mapIsNull();
}
