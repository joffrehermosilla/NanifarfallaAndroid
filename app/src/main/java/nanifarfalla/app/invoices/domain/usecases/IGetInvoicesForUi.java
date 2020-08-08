package nanifarfalla.app.invoices.domain.usecases;

import android.support.annotation.NonNull;

import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;

import java.util.List;

/**
 * Interfaz para estandarizar la ejecución del caso de uso para obtener facturas parciales
 */

public interface IGetInvoicesForUi {
    interface ExecuteCallback {
        void onSuccess(List<InvoiceUi> invoicesForUi);

        void onError(String error);

    }

    void execute(@NonNull Query query, boolean refresh, ExecuteCallback callback);
}
