package nanifarfalla.app.products.domain.criteria;

import android.net.Uri;

import nanifarfalla.app.selection.specification.MemorySpecification;
import nanifarfalla.app.selection.specification.ProviderSpecification;
import nanifarfalla.app.external.sqlite.DatabaseContract.Products;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Criterio para obtener todos los productos
 */

public class ProductsNoFilter
        implements MemorySpecification<Product>,
        ProviderSpecification {
    @Override
    public boolean isSatisfiedBy(Product item) {
        return true;
    }

    @Override
    public Uri asProvider() {
        return Products.buildUri();
    }
}
