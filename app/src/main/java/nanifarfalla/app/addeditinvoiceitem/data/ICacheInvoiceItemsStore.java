package nanifarfalla.app.addeditinvoiceitem.data;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;
import nanifarfalla.app.invoices.domain.entities.InvoiceItem;

/**
 * Estandar para caché de items de factura
 */

public interface ICacheInvoiceItemsStore {
    interface LoadInvoiceItemsUiCallback {

        void onInvoiceItemsLoaded(List<InvoiceItemUi> invoiceItemUis);

        void onDataNotAvailable();

    }

    interface GetInvoiceItemUiCallback {

        void onInvoiceItemUiLoaded(InvoiceItemUi invoiceItemUi);

        void onDataNotAvailable();

    }

    List<InvoiceItem> getInvoiceItems();

    void getInvoiceItemUi(@NonNull String productId, @NonNull GetInvoiceItemUiCallback callback);

    void getInvoiceItemsUi(@NonNull LoadInvoiceItemsUiCallback callback);

    void saveInvoiceItem(@NonNull InvoiceItem invoiceItem);

    void deleteInvoiceItem(@NonNull String productId);

    void deleteAll();

    float getTotal();

    float getSubtotal();

    float getTax();

}
