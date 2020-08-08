package nanifarfalla.app.invoices.domain.usecases;

import android.support.annotation.NonNull;

import nanifarfalla.app.invoices.data.IInvoicesRepository;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación de caso de uso "Obtener facturas parciales"
 */

public class GetInvoicesForUi implements IGetInvoicesForUi {

    private IInvoicesRepository mInvoicesRepository;


    public GetInvoicesForUi(@NonNull IInvoicesRepository invoicesRepository) {
        mInvoicesRepository = checkNotNull(invoicesRepository,
                "invoicesRepository no puede ser null");
    }

    @Override
    public void execute(@NonNull Query query, boolean refresh, final ExecuteCallback callback) {
        checkNotNull(query, "query no puede ser null");
        checkNotNull(callback, "callback no puede ser null");

        mInvoicesRepository.getInvoicesUis(query,
                new IInvoicesRepository.GetInvoicesUiCallback() {
                    @Override
                    public void onInvoicesInfoLoaded(List<InvoiceUi> invoicesForUi) {
                        checkNotNull(invoicesForUi, "invoicesInfos no puede ser null");
                        callback.onSuccess(invoicesForUi);
                    }

                    @Override
                    public void onDataNotAvailable(String errorMsg) {
                        callback.onError(errorMsg);
                    }
                });
    }
}
