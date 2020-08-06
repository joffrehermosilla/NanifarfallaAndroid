package nanifarfalla.app.invoices.presentation;

import com.hermosaprogramacion.premium.appproductos.invoices.domain.entities.InvoiceUi;

import java.util.List;

/**
 * Contrato MVP para la lista de facturas
 */

public interface InvoicesListMvp {
    interface View {
        void showInvoices(List<InvoiceUi> invoices);

        void showAddInvoice();

        void showLoadingState(boolean show);

        void showEmptyState();

        void showInvoicesError(String msg);

        void showInvoicesPage(List<InvoiceUi> invoices);

        void showLoadMoreIndicator(boolean show);

        void showEndlessScroll(boolean show);

        void showSuccessfullySavedMessage();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void loadInvoices(boolean refresh, boolean resume);

        void addNewInvoice();

        void manageSavingResult(int requestCode, int resultCode);
    }
}
