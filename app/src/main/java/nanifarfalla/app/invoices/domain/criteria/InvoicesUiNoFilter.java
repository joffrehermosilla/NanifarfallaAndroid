package nanifarfalla.app.invoices.domain.criteria;

import com.hermosaprogramacion.premium.appproductos.invoices.domain.entities.InvoiceUi;
import com.hermosaprogramacion.premium.appproductos.selection.specification.MemorySpecification;

/**
 * Especificación para todos las facturas parciales
 */

public class InvoicesUiNoFilter implements MemorySpecification<InvoiceUi> {
    @Override
    public boolean isSatisfiedBy(InvoiceUi item) {
        return true;
    }
}
