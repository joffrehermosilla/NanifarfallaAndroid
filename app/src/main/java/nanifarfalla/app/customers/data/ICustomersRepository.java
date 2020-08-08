package nanifarfalla.app.customers.data;

import androidx.annotation.NonNull;

import java.util.List;

import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.Query;

/**
 * Estandariza los accesos a los clientes
 */

public interface ICustomersRepository {
    interface GetCustomersCallback {
        void onCustomersLoaded(List<Customer> customers);

        void onDataNotAvailable(String errorMsg);
    }

    void getCustomers(@NonNull Query query, GetCustomersCallback callback);
}
