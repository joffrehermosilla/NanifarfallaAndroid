package nanifarfalla.app.products.data.datasource.local;

import android.content.ContentResolver;
import android.content.ContentValues;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import nanifarfalla.app.selection.selector.ProviderSelector;
import nanifarfalla.app.selection.specification.ProviderSpecification;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.external.sqlite.DatabaseContract.Products;
import nanifarfalla.app.products.domain.criteria.ProductsSelector;
import nanifarfalla.app.products.domain.model.Product;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación concreta del almacén de datos
 */

public class LocalProductsDataSource implements ILocalProductsDataSource {
    private static LocalProductsDataSource INSTANCE;

    private ContentResolver mContentResolver;

    public LocalProductsDataSource(@NonNull ContentResolver contentResolver) {
        mContentResolver = checkNotNull(contentResolver);
    }

    public static LocalProductsDataSource getInstance(ContentResolver contentResolver) {
        if (INSTANCE == null) {
            INSTANCE = new LocalProductsDataSource(contentResolver);
        }
        return INSTANCE;
    }


    @Override
    public void get(@NonNull Query query, @NonNull final GetCallback getCallback) {
        checkNotNull(query, "query no puede ser null");

        ProductsSelector selector = new ProductsSelector(query);
        selector.selectProviderRows(mContentResolver,
                new ProviderSelector.ProviderSelectorCallback<Product>() {
                    @Override
                    public void onDataSelected(List<Product> items) {
                        getCallback.onProductsLoaded(items);
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        getCallback.onDataNotAvailable(error);
                    }
                });

    }

    @Override
    public void save(@NonNull Product product) {
        checkNotNull(product);

        ContentValues values = new ContentValues();
        values.put(Products.CODE, product.getCode());
        values.put(Products.NAME, product.getName());
        values.put(Products.DESCRIPTION, product.getDescription());
        values.put(Products.BRAND, product.getBrand());
        values.put(Products.PRICE, product.getPrice());
        values.put(Products.UNITS_IN_STOCK, product.getUnitsInStock());
        values.put(Products.IMAGE_URL, product.getImageUrl());

        AsyncOpsProductsHandler handler = new AsyncOpsProductsHandler(mContentResolver);
        handler.startInsert(0,null,Products.buildUri(), values);
    }

    @Override
    public void delete(@Nullable ProviderSpecification specification) {
        if (specification == null) {
            mContentResolver.delete(Products.buildUri(), null, null);
        } else {
            mContentResolver.delete(specification.asProvider(), null, null);
        }
    }

}
