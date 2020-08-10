package nanifarfalla.app.products.presentation;

import com.google.common.base.Preconditions;

import java.util.List;

import nanifarfalla.app.login.data.IUsersRepository;
import nanifarfalla.app.products.domain.criteria.ProductsNoFilter;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.products.domain.usecases.IGetProducts;
import nanifarfalla.app.selection.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presentador que escucha los eventos de la UI y luego presenta los resultados a la vista
 */
public class ProductsPresenter implements ProductsMvp.Presenter {

    private final ProductsMvp.View mProductsView;
    private final IGetProducts mGetProducts;
    private final IUsersRepository mUsersRepository;

    private static final int PRODUCTS_LIMIT = 20;

    private boolean mIsFirstLoad = true;
    private int mCurrentPage = 1;


    public ProductsPresenter(ProductsMvp.View productsView, IGetProducts getProducts,
                             IUsersRepository usersRepository) {
        mProductsView = checkNotNull(productsView, "productsView no puede ser null");
        mGetProducts = checkNotNull(getProducts, "getProducts no puede ser null");
        mUsersRepository = checkNotNull(usersRepository, "usersRepository no puede ser null");
    }

    @Override
    public void loadProducts(final boolean reload) {

        if (reload || mIsFirstLoad) {
            mProductsView.showLoadingState(true);
            mCurrentPage = 1; // Reset...
        } else {
            mProductsView.showLoadMoreIndicator(true);
            mCurrentPage++;
        }

        // Construir Query...
        Query query = new Query(
                new ProductsNoFilter(), // Filtro
                "name", Query.ASC_ORDER,        // Orden
                mCurrentPage, PRODUCTS_LIMIT);  // Paginado

        // Retornar Productos...
        mGetProducts.getProducts(query, reload, new IGetProducts.GetProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                mProductsView.showLoadingState(false);
                processProducts(products, reload||mIsFirstLoad);
                mIsFirstLoad = false;

            }

            @Override
            public void onError(String error) {
                mProductsView.showLoadingState(false);
                mProductsView.showLoadMoreIndicator(false);
                mProductsView.showProductsError(error);
            }
        });

    }

    @Override
    public void openProductDetails(String productCode) {
        Preconditions.checkNotNull(productCode, "productCode no puede ser null");
        mProductsView.showProductDetailScreen(productCode);
    }

    @Override
    public void logOut() {
        mUsersRepository.closeSession();
        mProductsView.showLoginScreen();
    }

    private void processProducts(List<Product> products, boolean reload) {
        if (products.isEmpty()) {
            if (reload) {
                mProductsView.showEmptyState();
            } else {
                mProductsView.showLoadMoreIndicator(false);
            }
            mProductsView.allowMoreData(false);
        } else {

            if (reload) {
                mProductsView.showProducts(products);
            } else {
                mProductsView.showLoadMoreIndicator(false);
                mProductsView.showProductsPage(products);
            }

            mProductsView.allowMoreData(true);
        }
    }
}
