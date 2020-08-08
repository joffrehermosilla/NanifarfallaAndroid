package nanifarfalla.app.customers.domain.usecases;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.Query;

/**
 * Abstracción del caso de uso para obtener clientes
 */

public interface IGetCustomers {

    interface ExecuteCallback {
        void onSuccess(List<Customer> customers);

        void onError(String error);

    }

    void execute(@NonNull Query query, boolean refresh, ExecuteCallback callback);
}
