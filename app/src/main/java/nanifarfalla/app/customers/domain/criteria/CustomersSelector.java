package nanifarfalla.app.customers.domain.criteria;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.selection.selector.ListSelector;
import nanifarfalla.app.selection.specification.MemorySpecification;
import nanifarfalla.app.util.CollectionsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Selector de clientes
 */

public class CustomersSelector implements ListSelector<Customer> {

    /**
     * Constantes para los campos del cliente
     */
    public static final String NAME_CUSTOMER_FIELD = "name";

    private final Query mQuery;

    public CustomersSelector(Query query) {
        mQuery = query;
    }

    @Override
    public List<Customer> selectListRows(List<Customer> items) {
        // Clientes finales
        List<Customer> resultingCustomers;

        // Especificación inicial
        final MemorySpecification<Customer> spec =
                (MemorySpecification<Customer>) mQuery.getSpecification();

        // Comparador inicial
        Comparator<Customer> comp = mNameComparator;

        // Filtrar
        resultingCustomers = filterItems(items, spec);

        // Ordenar
        if (mQuery.getFieldSort() != null) {
            switch (mQuery.getFieldSort()) {
                case NAME_CUSTOMER_FIELD:
                    comp = mNameComparator;
                    break;
            }
        }
        Collections.sort(resultingCustomers, comp);

        // Paginar
        resultingCustomers = CollectionsUtils.getPage(resultingCustomers,
                mQuery.getPageNumber(), mQuery.getPageSize());

        return resultingCustomers;
    }

    @NonNull
    private ArrayList<Customer> filterItems(List<Customer> items,
                                            final MemorySpecification<Customer> spec) {
        Collection<Customer> filteredItems =
                Collections2.filter(items, new Predicate<Customer>() {
                    @Override
                    public boolean apply(Customer customer) {
                        return spec == null || spec.isSatisfiedBy(customer);
                    }
                });

        return new ArrayList<>(filteredItems);
    }

    // Comparador para el nombre del cliente
    private Comparator<Customer> mNameComparator = new Comparator<Customer>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            if (mQuery.getSortOrder() == Query.ASC_ORDER) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return o2.getName().compareTo(o1.getName());
            }
        }
    };
}
