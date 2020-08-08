package nanifarfalla.app.productdetail.domain.criteria;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import nanifarfalla.app.selection.specification.ProviderSpecification;
import nanifarfalla.app.external.sqlite.DatabaseContract.Products;
import nanifarfalla.app.selection.specification.MemorySpecification;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Criterio para obtener productos por ID
 */

public class ProductByCode implements MemorySpecification<Product>,
        ProviderSpecification {

    private String mProductCode;

    public ProductByCode(@NonNull String productCode) {
        mProductCode = Preconditions.checkNotNull(productCode,
                "productCode no puede ser null");
    }

    @Override
    public boolean isSatisfiedBy(Product product) {
        return mProductCode.equals(product.getCode());
    }


    @Override
    public Uri asProvider() {
        return Products.buildUriWith(mProductCode);
    }
}
