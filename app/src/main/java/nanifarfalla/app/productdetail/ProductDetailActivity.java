package nanifarfalla.app.productdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import nanifarfalla.app.R;
import nanifarfalla.app.di.DependencyProvider;
import nanifarfalla.app.productdetail.presentation.ProductDetailFragment;
import nanifarfalla.app.productdetail.presentation.ProductDetailPresenter;

public class ProductDetailActivity extends AppCompatActivity
        implements ProductDetailFragment.FragmentTransmitter {

    public static final String EXTRA_PRODUCT_CODE = "extra.product_code";

    // Referencias UI
    private ImageView mProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        // Crear fragmento
        ProductDetailFragment fragment = (ProductDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_product_detail_container);
        if (fragment == null) {
            fragment = ProductDetailFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_product_detail_container, fragment)
                    .commit();
        }

        // Obtener código del producto enviado desde la actividad de productos
        String productCode = getIntent().getStringExtra(EXTRA_PRODUCT_CODE);

        // Crear presentador
        ProductDetailPresenter detailPresenter =
                new ProductDetailPresenter(productCode, fragment,
                        DependencyProvider.provideGetProducts(getApplicationContext()));

        fragment.setPresenter(detailPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void passProductImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(mProductImage);
    }
}
