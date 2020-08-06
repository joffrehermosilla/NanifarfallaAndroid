package nanifarfalla.app.addeditinvoice.presentation;

import android.support.annotation.NonNull;

import com.hermosaprogramacion.premium.appproductos.addeditinvoice.domain.entities.InvoiceItemUi;

import java.util.Date;
import java.util.List;

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
