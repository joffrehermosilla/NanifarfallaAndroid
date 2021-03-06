package nanifarfalla.app.addeditinvoice.presentation;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;

/**
 * Contrato MVP para "Crear factura"
 */

public interface AddEditInvoiceMvp {

    interface View {
        void showCustomer(String name);

        void showCustomerError();

        void showItemsError();

        void showInvoicesScreen(String invoiceId);

        void showCustomersScreen();

        void showEditInvoiceItemScreen(@NonNull String productId);

        void showAddInvoiceItemScreen();

        void showInvoiceItems(List<InvoiceItemUi> invoiceItemUis);

        void showInvoiceAmounts(String subtotal, String tax, String total);

        void showSaveError(String error);

        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void saveInvoice(String customerId, Date date);

        void selectCustomer();

        void addNewInvoiceItem();

        void editInvoiceItem(String productId);

        void deleteInvoiceItem(String productId);

        void manageCustomerPickingResult(String customerId);

        void manageAdditionResult();

        void manageEditionResult();

        void loadInvoiceItems();

        void restoreState(@NonNull String customerId);
    }
}
