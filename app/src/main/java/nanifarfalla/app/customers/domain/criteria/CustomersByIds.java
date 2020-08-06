package nanifarfalla.app.customers.domain.criteria;

import com.google.common.base.Preconditions;
import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.selection.specification.MemorySpecification;

import java.util.List;

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
