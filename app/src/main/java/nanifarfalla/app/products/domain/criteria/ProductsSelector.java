package nanifarfalla.app.products.domain.criteria;

import android.content.ContentResolver;
import android.database.Cursor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import nanifarfalla.app.selection.selector.ListSelector;
import nanifarfalla.app.selection.specification.MemorySpecification;
import nanifarfalla.app.selection.selector.ProviderSelector;
import nanifarfalla.app.selection.specification.ProviderSpecification;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.selection.selector.RetrofitSelector;
import nanifarfalla.app.external.sqlite.DatabaseContract.Products;
import nanifarfalla.app.products.data.datasource.local.AsyncOpsProductsHandler;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.util.CollectionsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Retrofit;

/**
 * Consulta de productos
 */

public class ProductsSelector implements
        ListSelector<Product>,
        ProviderSelector<Product>,
        RetrofitSelector<Product> {

    private final Query mQuery;

    public ProductsSelector(Query query) {
        mQuery = query;
    }

    @Override
    public List<Product> selectListRows(List<Product> items) {
        final MemorySpecification<Product> memorySpecification
                = (MemorySpecification<Product>) mQuery.getSpecification();
        List<Product> affectedProducts;
        Comparator<Product> comparator = mCodeComparator;

        // Filtrar Por...
        affectedProducts = new ArrayList<>(
                Collections2.filter(items, new Predicate<Product>() {
                    @Override
                    public boolean apply(Product product) {
                        return memorySpecification == null
                                || memorySpecification.isSatisfiedBy(product);
                    }
                }));

        // Ordenar Por...
        if (mQuery.getFieldSort() != null) {
            switch (mQuery.getFieldSort()) {
                case "name":
                    comparator = mNameComparator;
                    break;
                // TODO: Añade un caso por cada comparador
            }
        }
        Collections.sort(affectedProducts, comparator);

        // Elegir Página...
        affectedProducts = CollectionsUtils.getPage(affectedProducts,
                mQuery.getPageNumber(), mQuery.getPageSize());

        return affectedProducts;
    }

    @Override
    public void selectProviderRows(ContentResolver contentResolver,
                                   final ProviderSelectorCallback<Product> callback) {
        ProviderSpecification specification = (ProviderSpecification) mQuery.getSpecification();

        // TODO: Traduce a parámetros y segmentos los atributos de mQuery y concatenalos a la URI

        AsyncOpsProductsHandler handler = new AsyncOpsProductsHandler(contentResolver);
        handler.setQueryListener(new AsyncOpsProductsHandler.AsyncOpListener() {
            @Override
            public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                List<Product> products = new ArrayList<>();

                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String code = cursor.getString(cursor.getColumnIndex(Products.CODE));
                        String name = cursor.getString(cursor.getColumnIndex(Products.NAME));
                        String description =
                                cursor.getString(cursor.getColumnIndex(Products.DESCRIPTION));
                        String brand = cursor.getString(cursor.getColumnIndex(Products.BRAND));
                        float price = cursor.getFloat(cursor.getColumnIndex(Products.PRICE));
                        int unitsInStock =
                                cursor.getInt(cursor.getColumnIndex(Products.UNITS_IN_STOCK));
                        String imageUrl = cursor.getString(cursor.getColumnIndex(Products.IMAGE_URL));
                        Product product = new Product(code, name, description,
                                brand, price, unitsInStock, imageUrl);
                        products.add(product);
                    }

                }

                if (cursor != null) {
                    cursor.close();
                }

                if (products.isEmpty()) {
                    callback.onDataNotAvailable("No se encontró ningún producto");
                } else {
                    callback.onDataSelected(products);
                }
            }
        });

        handler.startQuery(0, null, specification.asProvider(), null, null, null, null);

    }

    @Override
    public void selectRetrofitRows(Retrofit retrofit, RetrofitSelectorCallback<Product> callback) {

    }


    private Comparator<Product> mCodeComparator = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            if (mQuery.getSortOrder() == Query.ASC_ORDER) {
                return o1.getCode().compareTo(o2.getCode());
            } else {
                return o2.getCode().compareTo(o1.getCode());
            }
        }
    };

    private Comparator<Product> mNameComparator = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            if (mQuery.getSortOrder() == Query.ASC_ORDER) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return o2.getName().compareTo(o1.getName());
            }
        }
    };

    // TODO: Agrega más comparadores si los necesitas
}
