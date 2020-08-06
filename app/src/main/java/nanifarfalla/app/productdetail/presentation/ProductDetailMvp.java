package nanifarfalla.app.productdetail.presentation;

/**
 * Contrato MVP para el detalle del producto.
 */

public interface ProductDetailMvp {
    interface View {
        void showImage(String imageUrl);
        void showName(String name);
        void showPrice(String price);
        void showUnitsInStock(String unitsInStock);
        void showDescription(String description);

        void showProgressIndicator(boolean show);

        void showLoadError(String error);
        void showEmptyState();

        void setPresenter(Presenter productDetailPresenter);

        boolean isActive();
    }

    interface Presenter{
        void loadProduct();
    }
}
