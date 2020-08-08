package nanifarfalla.app.invoices.domain.usecases;

import android.support.annotation.NonNull;

import nanifarfalla.app.invoices.domain.entities.Invoice;

/**
 * Interfaz para estandarizar el guardado de facturas
 */

public interface ISaveInvoice {

    interface ExecuteCallback {
        void onSuccess(Invoice invoice);

        void onError(String error);

    }

    void execute(@NonNull Invoice invoice, ExecuteCallback callback);
}
