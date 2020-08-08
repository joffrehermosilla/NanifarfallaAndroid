package nanifarfalla.app.products.data.datasource.memory;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.products.domain.criteria.ProductsSelector;
import nanifarfalla.app.products.domain.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación concreta de la fuente de datos en memoria
 */
public class MemoryProductsDataSource implements IMemoryProductsDataSource {

    private static MemoryProductsDataSource INSTANCE = null;

    private static HashMap<String, Product> mCachedProducts = null;

    private MemoryProductsDataSource() {
    }

    public static MemoryProductsDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MemoryProductsDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Product> find(@NonNull Query query) {
        checkNotNull(query, "query no puede ser null");

        ArrayList<Product> products = Lists.newArrayList(mCachedProducts.values());
        ProductsSelector selector = new ProductsSelector(query);
        return selector.selectListRows(products);
    }

    @Override
    public void save(Product product) {
        if (mCachedProducts == null) {
            mCachedProducts = new LinkedHashMap<>();
        }
        mCachedProducts.put(product.getCode(), product);
    }


    @Override
    public void deleteAll() {
        if (mCachedProducts == null) {
            mCachedProducts = new LinkedHashMap<>();
        }
        mCachedProducts.clear();
    }


    @Override
    public boolean mapIsNull() {
        return mCachedProducts == null;
    }
}
