package nanifarfalla.app.customers.data.cache;

import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.Query;

import java.util.List;

/**
 * Interfaz con operaciones estandar en caché sobre los clientes
 */

public interface ICacheCustomersStore {
    List<Customer> getCustomers(Query query);

    void addCustomer(Customer customer);

    void deleteCustomers();

    boolean isCacheReady();
}
