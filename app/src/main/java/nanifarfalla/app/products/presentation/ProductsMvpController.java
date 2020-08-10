package nanifarfalla.app.products.presentation;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.common.base.Preconditions;

import nanifarfalla.app.R;
import nanifarfalla.app.di.DependencyProvider;
import nanifarfalla.app.productdetail.presentation.ProductDetailFragment;
import nanifarfalla.app.productdetail.presentation.ProductDetailPresenter;
import nanifarfalla.app.products.ProductsActivity;
import nanifarfalla.app.util.ActivityUtils;

//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;

/**
 * Crea los fragmentos de la vista y gestiona el comportamiento de sus presentadores
 */

public class ProductsMvpController {

    private final FragmentActivity mProductstActivity;

    @Nullable
    private final String mProductCode;
    private final boolean mActionPick;

    private ProductsTabletPresenter mProductsTabletPresenter;

    private ProductsPresenter mProductsPresenter;

    private ProductsMvpController(@NonNull ProductsActivity productstActivity,
                                  @Nullable String productCode,
                                  boolean actionPick) {
        mProductstActivity = productstActivity;
        mProductCode = productCode;
        mActionPick = actionPick;
    }

    public static ProductsMvpController createProductsMvp(@NonNull ProductsActivity productstActivity,
                                                          @Nullable String productCode,
                                                          boolean actionPick) {
        Preconditions.checkNotNull(productstActivity);

        ProductsMvpController productsMvpController =
                new ProductsMvpController(productstActivity, productCode, actionPick);

        productsMvpController.initProductsMvp();

        return productsMvpController;
    }

    private void initProductsMvp() {
        if (ActivityUtils.isTwoPane(mProductstActivity) && !mActionPick) {
            createTabletElements();
        } else {
            createPhoneElements();
        }
    }

    private void createTabletElements() {
        // Fragmento 1: Lista
        ProductsFragment productsFragment = findOrCreateProductsFragment(R.id.contentFrame_list);
        mProductsPresenter = createListPresenter(productsFragment);

        // Fragment 2: Detalle
        ProductDetailFragment productDetailFragment = findOrCreateProductDetailFragmentForTablet();
        ProductDetailPresenter productDetailPresenter = createProductDetailPresenter(productDetailFragment);

        // Conectar ambos fragmentos con el presentador de tablets
        mProductsTabletPresenter = new ProductsTabletPresenter(
                mProductsPresenter);

        productsFragment.setPresenter(mProductsTabletPresenter);
        productDetailFragment.setPresenter(mProductsTabletPresenter);
        mProductsTabletPresenter.setProductDetailPresenter(productDetailPresenter);
    }

    private void createPhoneElements() {
        ProductsFragment productsFragment = findOrCreateProductsFragment(R.id.contentFrame_list);
        mProductsPresenter = createListPresenter(productsFragment);
        productsFragment.setPresenter(mProductsPresenter);
    }

    private ProductsFragment findOrCreateProductsFragment(int container) {
        ProductsFragment productsFragment = (ProductsFragment)
                getSupportFragmentManager().findFragmentById(container);

        if (productsFragment == null) {
            productsFragment = ProductsFragment.newInstance(mActionPick);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(container, productsFragment)
                    .commit();
        }

        return productsFragment;
    }

    private ProductDetailFragment findOrCreateProductDetailFragmentForTablet() {
        ProductDetailFragment detailFragment = (ProductDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame_detail);
        if (detailFragment == null) {
            detailFragment = ProductDetailFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentFrame_detail, detailFragment, "FragmentDetail")
                    .commit();
        }
        return detailFragment;
    }

    private ProductsPresenter createListPresenter(ProductsMvp.View productsView) {
        return new ProductsPresenter(
                productsView,
                DependencyProvider.provideGetProducts(mProductstActivity),
                DependencyProvider.provideUsersRepository(mProductstActivity));

    }

    private ProductDetailPresenter createProductDetailPresenter(
            ProductDetailFragment productDetailFragment) {
        return new ProductDetailPresenter(
                mProductCode,
                productDetailFragment,
                DependencyProvider.provideGetProducts(mProductstActivity));
    }

    private FragmentManager getSupportFragmentManager() {
        return mProductstActivity.getSupportFragmentManager();
    }

    @Nullable
    public String getProductCode() {
        return mProductCode;
    }
}
