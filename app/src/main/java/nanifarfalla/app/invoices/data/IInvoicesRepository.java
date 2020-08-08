package nanifarfalla.app.invoices.data;

import android.support.annotation.NonNull;

import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;

import java.util.List;

/**
 * Interfaz para estandarizar las operaciones sobre facturas
 */

public interface IInvoicesRepository {
    interface GetInvoicesCallback {
        void onInvoicesLoaded(List<Invoice> invoices);

        void onDataNotAvailable(String errorMsg);
    }

    interface GetInvoicesUiCallback {
        void onInvoicesInfoLoaded(List<InvoiceUi> invoicesForUi);

        void onDataNotAvailable(String errorMsg);
    }


    void getInvoices(@NonNull Query query, @NonNull GetInvoicesCallback callback);

    void getInvoicesUis(@NonNull Query query, @NonNull GetInvoicesUiCallback callback);

    void saveInvoice(@NonNull Invoice invoice);
}
