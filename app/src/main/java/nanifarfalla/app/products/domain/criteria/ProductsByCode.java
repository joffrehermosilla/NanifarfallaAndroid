package nanifarfalla.app.products.domain.criteria;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.specification.MemorySpecification;
import nanifarfalla.app.selection.specification.ProviderSpecification;

import java.util.List;

/**
 * Obtener productos por donde el código sea...
 */

public class ProductsByCode implements MemorySpecification<Product>, ProviderSpecification {

    private List<String> mCodes;

    public ProductsByCode(@NonNull List<String> codes) {
        mCodes = Preconditions.checkNotNull(codes);
    }

    @Override
    public boolean isSatisfiedBy(Product item) {
        boolean satisfied = false;
        for(String code: mCodes){
            satisfied = satisfied || code.equals(item.getCode());
        }
        return satisfied;
    }

    @Override
    public Uri asProvider() {
        // TODO: Implementar
        return null;
    }
}
