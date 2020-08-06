package nanifarfalla.app.data.products.datasource.memory;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nanifarfalla.app.products.domain.criteria.ProductCriteria;
import nanifarfalla.app.products.domain.model.Product;

/**
 * Implementación concreta de la fuente de datos en memoria
 */
public class MemoryProductsDataSource implements IMemoryProductsDataSource {
    private static HashMap<String, Product> mCachedProducts;

    @Override
    public List<Product> find(ProductCriteria criteria) {

        ArrayList<Product> products =
                Lists.newArrayList(mCachedProducts.values());
        return criteria.match(products);
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
