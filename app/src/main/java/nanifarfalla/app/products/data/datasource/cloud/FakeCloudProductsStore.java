package nanifarfalla.app.products.data.datasource.cloud;

import android.os.Handler;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import com.google.common.collect.Lists;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fuente de datos relacionada al servidor remoto
 */
public class FakeCloudProductsStore implements ICloudProductsDataSource {

    private static FakeCloudProductsStore INSTANCE = null;
    private static HashMap<String, Product> API_DATA;

    static {
        API_DATA = new LinkedHashMap<>();
        for (int i = 0; i < 1000; i++) {
            addProduct(UUID.randomUUID().toString(), "Producto " + (i + 1), "", "", 4.8f, 7,
                    "file:///android_asset/mock-product.png");
        }
    }

    private static void addProduct(String code, String name, String description,
                                   String brand, float price, int unitsInStock, String imageUrl) {
        Product newProduct = new Product(code, name, description,
                brand, price, unitsInStock, imageUrl);
        API_DATA.put(newProduct.getCode(), newProduct);
    }


    private FakeCloudProductsStore() {


    }

    public static FakeCloudProductsStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeCloudProductsStore();
        }
        return INSTANCE;
    }

    @Override
    public void getProducts(@NonNull Query query, String userToken,
                            final ProductServiceCallback callback) {
        checkNotNull(query, "query no puede ser null");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onLoaded(Lists.newArrayList(API_DATA.values()));
            }
        }, 2000);

    }
}
