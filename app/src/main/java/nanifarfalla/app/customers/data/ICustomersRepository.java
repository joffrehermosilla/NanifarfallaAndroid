package nanifarfalla.app.customers.data;

import android.support.annotation.NonNull;

import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.Query;

import java.util.List;

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
