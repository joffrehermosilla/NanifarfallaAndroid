package nanifarfalla.app.products.presentation;

import com.hermosaprogramacion.premium.appproductos.products.domain.model.Product;

import java.util.List;

/**
 * Contrato MVP para la lista de productos
 */
public interface ProductsMvp {
    interface View {
        void showProducts(List<Product> products);

        void showLoadingState(boolean show);

        void showEmptyState();

        void showProductsError(String msg);

        void showProductsPage(List<Product> products);

        void showLoadMoreIndicator(boolean show);

        void allowMoreData(boolean show);

        void showLoginScreen();

        void showProductDetailScreen(String productCode);

        void setPresenter(Presenter presenter);

        Presenter getPresenter();
    }

    interface Presenter {
        void loadProducts(boolean reload);

        void openProductDetails(String productCode);

        void logOut();
    }
}
