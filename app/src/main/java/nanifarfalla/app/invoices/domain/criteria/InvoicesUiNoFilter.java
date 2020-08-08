package nanifarfalla.app.invoices.domain.criteria;

import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.specification.MemorySpecification;

/**
 * Especificación para todos las facturas parciales
 */

public class InvoicesUiNoFilter implements MemorySpecification<InvoiceUi> {
    @Override
    public boolean isSatisfiedBy(InvoiceUi item) {
        return true;
    }
}
