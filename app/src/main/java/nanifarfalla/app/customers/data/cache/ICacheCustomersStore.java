package nanifarfalla.app.customers.data.cache;

import java.util.List;

import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.Query;

/**
 * Interfaz con operaciones estandar en caché sobre los clientes
 */

public interface ICacheCustomersStore {
    List<Customer> getCustomers(Query query);

    void addCustomer(Customer customer);

    void deleteCustomers();

    boolean isCacheReady();
}
