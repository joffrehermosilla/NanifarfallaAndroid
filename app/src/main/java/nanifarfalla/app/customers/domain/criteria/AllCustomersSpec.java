package nanifarfalla.app.customers.domain.criteria;

import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.specification.MemorySpecification;

/**
 * Criterio para obtener todos los clientes
 */

public class AllCustomersSpec implements MemorySpecification<Customer> {
    @Override
    public boolean isSatisfiedBy(Customer item) {
        return true;
    }
}
