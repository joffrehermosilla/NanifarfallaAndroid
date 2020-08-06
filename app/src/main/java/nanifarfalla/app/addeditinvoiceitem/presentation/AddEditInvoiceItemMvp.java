package nanifarfalla.app.addeditinvoiceitem.presentation;

/**
 * Contrato Mvp para "Añadir/Editar items de factura"
 */

public interface AddEditInvoiceItemMvp {
    interface View {
        void showProductName(String productName);

        void showStock(String stock);

        void showProductNotSelectedError();

        void showMissingProduct();

        void showQuantityError();

        void showProductsScreen();

        void showAddEditInvoiceScreen();

        void showQuantity(String quantity);

        void showMissingInvoiceItem();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        void saveInvoiceItem(String selectedProductId, String productName, String quantityString);

        void selectProduct();

        void manageProductPickingResult(String productId);

        void restoreState(String productId);

        void populateInvoiceItem();
    }
}
