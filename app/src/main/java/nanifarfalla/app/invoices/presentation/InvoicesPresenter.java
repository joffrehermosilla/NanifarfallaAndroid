package nanifarfalla.app.invoices.presentation;

import android.app.Activity;

import com.hermosaprogramacion.premium.appproductos.invoices.InvoicesActivity;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.criteria.InvoicesUiNoFilter;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.criteria.InvoicesUiSelector;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.entities.InvoiceUi;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.usecases.IGetInvoicesForUi;
import com.hermosaprogramacion.premium.appproductos.selection.Query;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presentador de la lista de facturas
 */

public class InvoicesPresenter implements InvoicesListMvp.Presenter {

    private InvoicesListMvp.View mView;
    private IGetInvoicesForUi mGetInvoicesForUi;

    private static final int INVOICES_PAGE_SIZE = 20;

    private int mCurrentPage = 1;

    private boolean mIsFirstLoad = true;

    public InvoicesPresenter(InvoicesListMvp.View invoicesView, IGetInvoicesForUi getInvoicesInfos) {
        mView = checkNotNull(invoicesView, "invoicesView");
        mGetInvoicesForUi = checkNotNull(getInvoicesInfos, "getInvoicesUis");
    }

    @Override
    public void loadInvoices(final boolean manualRefresh, final boolean resume) {

        final boolean normalLoad = manualRefresh || mIsFirstLoad || resume;

        if (normalLoad) {
            mView.showLoadingState(true);
            mCurrentPage = 1; // Reset del páginado
        } else {
            mView.showLoadMoreIndicator(true);
            mCurrentPage++; // Preparar página siguiente
        }

        // Crear consulta de facturas parciales
        Query query = new Query(
            /* Filtro */    new InvoicesUiNoFilter(),
            /* Orden */     InvoicesUiSelector.DATE_INVOICE_FIELD, Query.DESC_ORDER,
            /* Paginado */  mCurrentPage, INVOICES_PAGE_SIZE);

        // Ejecutar caso de uso "Obtener Facturas parciales"
        mGetInvoicesForUi.execute(query, manualRefresh || mIsFirstLoad,
                new IGetInvoicesForUi.ExecuteCallback() {
                    @Override
                    public void onSuccess(List<InvoiceUi> invoicesForUi) {
                        mView.showLoadingState(false);

                        if (invoicesForUi.isEmpty()) {
                            if (normalLoad) {
                                mView.showEmptyState();
                            } else {
                                mView.showLoadMoreIndicator(false);
                            }
                            mView.showEndlessScroll(false);

                        } else {
                            if (normalLoad) {
                                mView.showInvoices(invoicesForUi);
                            } else {
                                mView.showLoadMoreIndicator(false);
                                mView.showInvoicesPage(invoicesForUi);
                            }
                            mView.showEndlessScroll(true);
                        }

                        mIsFirstLoad = false; // Off
                    }

                    @Override
                    public void onError(String error) {
                        mView.showLoadingState(false);
                        mView.showLoadMoreIndicator(false);
                        mView.showInvoicesError(error);
                    }
                });

    }

    @Override
    public void addNewInvoice() {
        mView.showAddInvoice();
    }

    @Override
    public void manageSavingResult(int requestCode, int resultCode) {
        if (InvoicesActivity.REQUEST_ADD_INVOICE == requestCode
                && Activity.RESULT_OK == resultCode) {
            mView.showSuccessfullySavedMessage();
        }
    }
}
