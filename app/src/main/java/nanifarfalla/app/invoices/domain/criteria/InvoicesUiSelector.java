package nanifarfalla.app.invoices.domain.criteria;

import android.support.annotation.NonNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.entities.InvoiceUi;
import com.hermosaprogramacion.premium.appproductos.selection.Query;
import com.hermosaprogramacion.premium.appproductos.selection.selector.ListSelector;
import com.hermosaprogramacion.premium.appproductos.selection.specification.MemorySpecification;
import com.hermosaprogramacion.premium.appproductos.util.CollectionsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Selección de facturas parciales
 */

public class InvoicesUiSelector implements ListSelector<InvoiceUi> {

    public static final String DATE_INVOICE_FIELD = "date";

    private Query mQuery;

    public InvoicesUiSelector(Query query) {
        mQuery = query;
    }

    @Override
    public List<InvoiceUi> selectListRows(List<InvoiceUi> items) {
        // Facturas finales
        List<InvoiceUi> resultingInvoices;

        // Especificación inicial
        final MemorySpecification<InvoiceUi> spec =
                (MemorySpecification<InvoiceUi>) mQuery.getSpecification();

        // Comparador inicial
        Comparator<InvoiceUi> comp = mDateComparator;

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
    private ArrayList<InvoiceUi> filterItems(List<InvoiceUi> items,
                                             final MemorySpecification<InvoiceUi> spec) {
        Collection<InvoiceUi> filteredItems =
                Collections2.filter(items, new Predicate<InvoiceUi>() {
                    @Override
                    public boolean apply(InvoiceUi invoice) {
                        return spec == null || spec.isSatisfiedBy(invoice);
                    }
                });

        return new ArrayList<>(filteredItems);
    }

    // Comparador para fecha
    private Comparator<InvoiceUi> mDateComparator = new Comparator<InvoiceUi>() {
        @Override
        public int compare(InvoiceUi o1, InvoiceUi o2) {
            if (mQuery.getSortOrder() == Query.ASC_ORDER) {
                return o1.getDate().compareTo(o2.getDate());
            } else {
                return o2.getDate().compareTo(o1.getDate());
            }
        }
    };
}
