package nanifarfalla.app.customers.domain.usecases;

import android.support.annotation.NonNull;

import com.hermosaprogramacion.premium.appproductos.customers.data.ICustomersRepository;
import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.Query;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación de "Obtener clientes"
 */

public class GetCustomers implements IGetCustomers {
    private final ICustomersRepository mCustomersRepository;

    public GetCustomers(ICustomersRepository customersRepository) {
        mCustomersRepository = checkNotNull(customersRepository,
                "customersRepository no puede ser null");
    }

    @Override
    public void execute(@NonNull Query query, boolean refresh, final ExecuteCallback callback) {
        checkNotNull(query, "query no puede ser null");
        checkNotNull(callback, "callback no puede ser null");

        mCustomersRepository.getCustomers(query, new ICustomersRepository.GetCustomersCallback() {
            @Override
            public void onCustomersLoaded(List<Customer> customers) {
                checkNotNull(customers, "customers no puede ser null");
                callback.onSuccess(customers);
            }

            @Override
            public void onDataNotAvailable(String errorMsg) {
                callback.onError(errorMsg);
            }
        });
    }
}
