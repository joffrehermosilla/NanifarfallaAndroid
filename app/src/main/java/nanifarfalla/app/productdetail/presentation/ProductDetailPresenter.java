package nanifarfalla.app.productdetail.presentation;

import com.google.common.base.Strings;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.productdetail.domain.criteria.ProductByCode;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.products.domain.usecases.IGetProducts;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación concreta del presentador de detalle del producto.
 */

public class ProductDetailPresenter implements ProductDetailMvp.Presenter {

    private String mProductCode;

    // Relaciones
    private ProductDetailMvp.View mProductDetailView;
    private IGetProducts mGetProducts;


    public ProductDetailPresenter(String productCode, ProductDetailMvp.View productDetailView,
                                  IGetProducts getProducts) {
        mProductCode = productCode;
        mProductDetailView = checkNotNull(productDetailView, "productDetailView no puede ser null");
        mGetProducts = checkNotNull(getProducts, "getProducts no puede ser null");
    }

    public String getProductCode() {
        return mProductCode;
    }

    public void setProductCode(String productCode) {
        mProductCode = productCode;
    }

    @Override
    public void loadProduct() {

        if (!mProductDetailView.isActive()) {
            return;
        }

        if (Strings.isNullOrEmpty(mProductCode)) {
            mProductDetailView.showEmptyState();
            return;
        }

        mProductDetailView.showProgressIndicator(true);

        Query detailQuery = new Query(
                new ProductByCode(mProductCode), null, 0, 0, 0);

        mGetProducts.getProducts(detailQuery, false, new IGetProducts.GetProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                if (!mProductDetailView.isActive()) {
                    return;
                }

                mProductDetailView.showProgressIndicator(false);
                showProduct(products.get(0));
            }

            @Override
            public void onError(String error) {
                if (!mProductDetailView.isActive()) {
                    return;
                }
                mProductDetailView.showProgressIndicator(false);
                mProductDetailView.showEmptyState();
                mProductDetailView.showLoadError(error);
            }
        });
    }

    private void showProduct(Product product) {
        mProductDetailView.showImage(product.getImageUrl());
        mProductDetailView.showName(product.getName());
        mProductDetailView.showPrice(product.getFormatedPrice());
        mProductDetailView.showUnitsInStock(
                String.format("%s unidades en existencia", product.getUnitsInStock()));
        mProductDetailView.showDescription(product.getDescription());
    }
}
