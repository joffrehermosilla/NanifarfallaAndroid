package nanifarfalla.app.customers.domain.criteria;

import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.specification.MemorySpecification;

/**
 * Criterio para obtener todos los clientes
 */

public class AllCustomersSpec implements MemorySpecification<Customer> {
    @Override
    public boolean isSatisfiedBy(Customer item) {
        return true;
    }
}
