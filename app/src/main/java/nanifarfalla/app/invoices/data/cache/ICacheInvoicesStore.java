package nanifarfalla.app.invoices.data.cache;

import java.util.List;

import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;

/**
 * Estandariza las operaciones estandar en caché sobre las facturas
 */

public interface ICacheInvoicesStore {

    interface LoadInvoicesUiCallback{
        void onInvoicesUiLoaded(List<InvoiceUi> invoiceUis);
        void onDataNotAvailable();
    }

    List<Invoice> getInvoices(Query query);

    void getInvoicesUis(Query query, LoadInvoicesUiCallback callback);

    void addInvoice(Invoice invoice);

    void deleteInvoices();

    boolean isCacheReady();
}
