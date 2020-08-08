package nanifarfalla.app.products.data.datasource.local;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.selection.specification.ProviderSpecification;

/**
 * Representación de fuente de datos local
 */

public interface ILocalProductsDataSource {
    interface GetCallback {
        void onProductsLoaded(List<Product> products);

        void onDataNotAvailable(String error);
    }

    void get(@NonNull Query query, @NonNull GetCallback getCallback);

    void save(@NonNull Product product);

    void delete(@Nullable ProviderSpecification specification);
}
