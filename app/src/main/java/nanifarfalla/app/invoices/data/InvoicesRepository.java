package nanifarfalla.app.invoices.data;

import androidx.annotation.NonNull;

import com.google.common.base.Preconditions;

import java.util.List;

import nanifarfalla.app.invoices.data.cache.ICacheInvoicesStore;
import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;

/**
 * Repositorio de facturas
 */

public class InvoicesRepository implements IInvoicesRepository {

    private static InvoicesRepository INSTANCE = null;

    private ICacheInvoicesStore mCacheInvoicesStore;

    private InvoicesRepository(@NonNull ICacheInvoicesStore cacheInvoicesStore) {
        mCacheInvoicesStore = Preconditions.checkNotNull(cacheInvoicesStore);
    }

    public static InvoicesRepository getInstance(@NonNull ICacheInvoicesStore cacheInvoicesStore) {
        if (INSTANCE == null) {
            INSTANCE = new InvoicesRepository(cacheInvoicesStore);
        }

        return INSTANCE;
    }

    @Override
    public void getInvoices(@NonNull Query query, @NonNull GetInvoicesCallback callback) {
        if (mCacheInvoicesStore.isCacheReady()) {
            callback.onInvoicesLoaded(mCacheInvoicesStore.getInvoices(query));
        }
    }

    @Override
    public void getInvoicesUis(@NonNull Query query, @NonNull final GetInvoicesUiCallback callback) {
        if (mCacheInvoicesStore.isCacheReady()) {
            mCacheInvoicesStore.getInvoicesUis(query,
                    new ICacheInvoicesStore.LoadInvoicesUiCallback() {
                        @Override
                        public void onInvoicesUiLoaded(List<InvoiceUi> invoiceUis) {
                            callback.onInvoicesInfoLoaded(invoiceUis);
                        }

                        @Override
                        public void onDataNotAvailable() {
                            callback.onDataNotAvailable("");
                        }
                    });
        }
    }

    @Override
    public void saveInvoice(@NonNull Invoice invoice) {
        Preconditions.checkNotNull(invoice);

        mCacheInvoicesStore.addInvoice(invoice);
    }
}
