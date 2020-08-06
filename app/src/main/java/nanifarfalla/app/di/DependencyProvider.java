package nanifarfalla.app.di;

import android.content.Context;

import androidx.annotation.NonNull;

import nanifarfalla.app.data.products.ProductsRepository;
import nanifarfalla.app.data.products.datasource.cloud.CloudProductsDataSource;
import nanifarfalla.app.data.products.datasource.memory.MemoryProductsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

//import android.support.annotation.NonNull;

/**
 * Contenedor de dependencias
 */
public final class DependencyProvider {

    private static Context mContext;
    private static MemoryProductsDataSource memorySource = null;
    private static CloudProductsDataSource cloudSource = null;
    private static ProductsRepository mProductsRepository = null;

    private DependencyProvider() {
    }

    public static ProductsRepository provideProductsRepository(@NonNull Context context) {
        mContext = checkNotNull(context);
        if (mProductsRepository == null) {
            mProductsRepository = new ProductsRepository(getMemorySource(),
                    getCloudSource(), context);
        }
        return mProductsRepository;
    }

    public static MemoryProductsDataSource getMemorySource() {
        if (memorySource == null) {
            memorySource = new MemoryProductsDataSource();
        }
        return memorySource;
    }

    public static CloudProductsDataSource getCloudSource() {
        if (cloudSource == null) {
            cloudSource = new CloudProductsDataSource();
        }
        return cloudSource;
    }
}
