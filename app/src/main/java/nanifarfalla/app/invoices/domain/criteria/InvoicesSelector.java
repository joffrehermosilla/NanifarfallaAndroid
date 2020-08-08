package nanifarfalla.app.invoices.domain.criteria;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import nanifarfalla.app.invoices.domain.entities.Invoice;
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
 * Selección de facturas
 */

public class InvoicesSelector implements ListSelector<Invoice> {

    public static final String DATE_INVOICE_FIELD = "date";

    private Query mQuery;

    public InvoicesSelector(Query query) {
        mQuery = query;
    }

    @Override
    public List<Invoice> selectListRows(List<Invoice> items) {
        // Facturas finales
        List<Invoice> resultingInvoices;

        // Especificación inicial
        final MemorySpecification<Invoice> spec =
                (MemorySpecification<Invoice>) mQuery.getSpecification();

        // Comparador inicial
        Comparator<Invoice> comp = mDateComparator;

        // Filtrar
        resultingInvoices = filterItems(items, spec);

        // Ordenar
        if (mQuery.getFieldSort() != null) {
            switch (mQuery.getFieldSort()) {
                case DATE_INVOICE_FIELD:
                    comp = mDateComparator;
                    break;
            }
        }
        Collections.sort(resultingInvoices, comp);

        // Paginar
        resultingInvoices = CollectionsUtils.getPage(resultingInvoices,
                mQuery.getPageNumber(), mQuery.getPageSize());

        return resultingInvoices;
    }

    @NonNull
    private ArrayList<Invoice> filterItems(List<Invoice> items,
                                           final MemorySpecification<Invoice> spec) {
        Collection<Invoice> filteredItems =
                Collections2.filter(items, new Predicate<Invoice>() {
                    @Override
                    public boolean apply(Invoice invoice) {
                        return spec == null || spec.isSatisfiedBy(invoice);
                    }
                });

        return new ArrayList<>(filteredItems);
    }

    // Comparador para fecha
    private Comparator<Invoice> mDateComparator = new Comparator<Invoice>() {
        @Override
        public int compare(Invoice o1, Invoice o2) {
            if (mQuery.getSortOrder() == Query.ASC_ORDER) {
                return o1.getDate().compareTo(o2.getDate());
            } else {
                return o2.getDate().compareTo(o1.getDate());
            }
        }
    };
}
