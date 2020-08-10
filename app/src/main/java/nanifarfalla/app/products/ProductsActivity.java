package nanifarfalla.app.products;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

//import nanifarfalla.app.BaseActivity;
import nanifarfalla.app.MainActivity;
import nanifarfalla.app.products.presentation.ProductsMvpController;
import nanifarfalla.app.R;

public class ProductsActivity extends MainActivity {
    public final static String ACTION_PICK_PRODUCT
            = "com.hermosaprogramacion.action.ACTION_PICK_PRODUCT";
    public static final String EXTRA_PRODUCT_ID = "com.hermosaprogramacion.EXTRA_PRODUCT_ID";

    private final static String BUNDLE_PRODUCT_CODE = "BUNDLE_PRODUCT_CODE";


    private ProductsMvpController mProductsMvpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean actionPick = isActionPick();

        setContentView(actionPick ? R.layout.activity_products_picking : R.layout.activity_products);

        setTitle(actionPick ? R.string.pick_product : R.string.products);

        // Obtener código del producto guardado en cambios de configuración.
        // Este no se usará a menos que uses master-detail en ambas orientaciones.
        String productCode = null;
        if (savedInstanceState != null) {
            productCode = savedInstanceState.getString(BUNDLE_PRODUCT_CODE);
        }

        // Crear controlador de productos
        mProductsMvpController =
                ProductsMvpController.createProductsMvp(this, productCode,actionPick );

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_PRODUCT_CODE, mProductsMvpController.getProductCode());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showActionPickNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_menu, menu);
        return true;
    }

    @Override
    protected NavDrawerItemEnum getNavDrawerItem() {
        return NavDrawerItemEnum.PRODUCTS;
    }

    private boolean isActionPick() {
        return ACTION_PICK_PRODUCT.equals(getIntent().getAction());
    }

    private void showActionPickNavigation() {
        if (isActionPick()) {
            setToolbarAsUp(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }
}
