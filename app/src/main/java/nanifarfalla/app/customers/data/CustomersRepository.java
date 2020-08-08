package nanifarfalla.app.customers.data;

import android.support.annotation.NonNull;

import nanifarfalla.app.customers.data.cache.CacheCustomersStore;
import nanifarfalla.app.selection.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación del repositorio de clientes
 */

public class CustomersRepository implements ICustomersRepository {

    private static CustomersRepository INSTANCE = null;

    private CacheCustomersStore mCacheCustomersStore;

    private CustomersRepository(@NonNull CacheCustomersStore cacheCustomersStore) {
        mCacheCustomersStore = checkNotNull(cacheCustomersStore);
    }

    public static CustomersRepository getInstance(CacheCustomersStore cacheCustomersStore) {
        if (INSTANCE == null) {
            INSTANCE = new CustomersRepository(cacheCustomersStore);
        }
        return INSTANCE;
    }

    @Override
    public void getCustomers(@NonNull Query query, GetCustomersCallback callback) {

        // Retornar datos existentes en caché
        if (mCacheCustomersStore.isCacheReady()) {
            callback.onCustomersLoaded(mCacheCustomersStore.getCustomers(query));
        }
    }
}
