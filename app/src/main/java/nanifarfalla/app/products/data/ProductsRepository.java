package nanifarfalla.app.products.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.login.data.preferences.IUserPreferences;
import nanifarfalla.app.products.data.datasource.cloud.ICloudProductsDataSource;
import nanifarfalla.app.products.data.datasource.local.ILocalProductsDataSource;
import nanifarfalla.app.products.data.datasource.memory.IMemoryProductsDataSource;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Repositorio de productos
 */
public class ProductsRepository implements IProductsRepository {

    private static ProductsRepository INSTANCE = null;

    private final IMemoryProductsDataSource mMemoryProductsDataSource;
    private final ILocalProductsDataSource mLocalProductsDataSource;
    private final ICloudProductsDataSource mCloudProductsDataSource;
    private final IUserPreferences mUserPreferences;

    private final ConnectivityManager mConnectivityManager;

    private boolean mReload = false;


    private ProductsRepository(@NonNull IMemoryProductsDataSource memoryDataSource,
                               @NonNull ILocalProductsDataSource localProductsDataSource,
                               @NonNull ICloudProductsDataSource cloudDataSource,
                               @NonNull IUserPreferences userPreferences,
                               Context context) {
        mMemoryProductsDataSource = checkNotNull(memoryDataSource);
        mLocalProductsDataSource = checkNotNull(localProductsDataSource);
        mCloudProductsDataSource = checkNotNull(cloudDataSource);
        mUserPreferences = checkNotNull(userPreferences);
        mConnectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    public static ProductsRepository getInstance(IMemoryProductsDataSource memoryDataSource,
                                                 ILocalProductsDataSource localProductsDataSource,
                                                 ICloudProductsDataSource cloudDataSource,
                                                 IUserPreferences userPreferences, Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductsRepository(memoryDataSource, localProductsDataSource, cloudDataSource,
                    userPreferences, context);
        }
        return INSTANCE;
    }

    @Override
    public void getProducts(@NonNull final Query query, final GetProductsCallback callback) {
        checkNotNull(query, "query no puede ser null");

        // ¿Hay Datos En Caché Y No Se Ordenó Recarga?
        if (!mMemoryProductsDataSource.mapIsNull() && !mReload) {
            callback.onProductsLoaded(mMemoryProductsDataSource.find(query));
            return;
        }

        // ¿Se ordenaron refrescar los datos?
        if (mReload) {
            getProductsFromServer(query, callback);
        } else {

            mLocalProductsDataSource.get(query, new ILocalProductsDataSource.GetCallback() {
                @Override
                public void onProductsLoaded(List<Product> products) {
                    refreshMemoryDataSource(products);
                    callback.onProductsLoaded(mMemoryProductsDataSource.find(query));
                }

                @Override
                public void onDataNotAvailable(String error) {
                    getProductsFromServer(query, callback);
                }
            });
        }
    }

    private void getProductsFromServer(final Query query, final GetProductsCallback callback) {

        if (!isOnline()) {
            // El Refresco No Pudo Ser
            mReload = false;
            callback.onDataNotAvailable("No hay conexión de red.");
            return;
        }

        // Obtener el token
        String token = mUserPreferences.getAccessToken();

        mCloudProductsDataSource.getProducts(
                query, token, new ICloudProductsDataSource.ProductServiceCallback() {
                    @Override
                    public void onLoaded(List<Product> products) {
                        refreshMemoryDataSource(products);
                        refreshLocalDataSource(products);
                        callback.onProductsLoaded(mMemoryProductsDataSource.find(query));
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable(error);
                    }
                }
        );
    }

    private void refreshMemoryDataSource(List<Product> products) {
        mMemoryProductsDataSource.deleteAll();
        for (Product product : products) {
            mMemoryProductsDataSource.save(product);
        }
        mReload = false;
    }

    private void refreshLocalDataSource(List<Product> products) {
        // TODO: Reemplaza la eliminación total por una comprobación refinada de sincronización
        mLocalProductsDataSource.delete(null);
        for (Product product : products) {
            mLocalProductsDataSource.save(product);
        }
        mReload = false;
    }

    private boolean isOnline() {
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    @Override
    public void refreshProducts() {
        mReload = true;
    }

}
