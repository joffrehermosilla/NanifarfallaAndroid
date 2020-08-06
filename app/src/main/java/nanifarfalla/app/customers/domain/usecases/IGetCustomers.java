package nanifarfalla.app.customers.domain.usecases;

import android.support.annotation.NonNull;

import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.Query;

import java.util.List;

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
