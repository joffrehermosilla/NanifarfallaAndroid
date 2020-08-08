package nanifarfalla.app.customers.domain.criteria;

import com.google.common.base.Preconditions;

import java.util.List;

import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.specification.MemorySpecification;

/**
 * Created by James on 30/01/2018.
 */

public class CustomersByIds implements MemorySpecification<Customer> {
    private List<String> mCustomersIds;

    public CustomersByIds(List<String> customersIds) {
        mCustomersIds = Preconditions.checkNotNull(customersIds,
                "customerId no puede ser null");
    }

    @Override
    public boolean isSatisfiedBy(Customer item) {
        boolean satisfied = false;
        for (String customerId : mCustomersIds) {
            satisfied = satisfied || customerId.equals(item.getId());
        }
        return satisfied;
    }
}
