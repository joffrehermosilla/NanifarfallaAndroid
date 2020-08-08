package nanifarfalla.app.customers.presentation;

import nanifarfalla.app.customers.domain.criteria.AllCustomersSpec;
import nanifarfalla.app.customers.domain.criteria.CustomersSelector;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.customers.domain.usecases.IGetCustomers;
import nanifarfalla.app.selection.Query;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presentador para la lista de clientes
 */

public class CustomersPresenter implements CustomersListMvp.Presenter {

    private CustomersListMvp.View mView;
    private IGetCustomers mGetCustomers;

    private static final int CUSTOMERS_PAGE_SIZE = 20;

    private int mCurrentPage = 1;

    private boolean mIsFirstLoad = true;

    public CustomersPresenter(CustomersListMvp.View customersView,
                              IGetCustomers getCustomers) {
        mView = checkNotNull(customersView,
                "customersView no puede ser null");
        mGetCustomers = checkNotNull(getCustomers,
                "getCustomers no puede ser null");
    }

    @Override
    public void loadCustomers(final boolean manualRefresh) {

        // Lógica para mostrar indicadores
        if (manualRefresh || mIsFirstLoad) {
            mView.showLoadingState(true);
            mCurrentPage = 1; // Reset del páginado
        } else {
            mView.showLoadMoreIndicator(true);
            mCurrentPage++; // Preparar página siguiente
        }

        // Resumir consulta de clientes
        Query query = new Query(
                /* Filtro */    new AllCustomersSpec(),
                /* Orden */     CustomersSelector.NAME_CUSTOMER_FIELD, Query.ASC_ORDER,
                /* Paginado */  mCurrentPage, CUSTOMERS_PAGE_SIZE);

        // Ejecutar caso de uso "Obtener Clientes"
        mGetCustomers.execute(query, manualRefresh || mIsFirstLoad, new IGetCustomers.ExecuteCallback() {
            @Override
            public void onSuccess(List<Customer> customers) {
                mView.showLoadingState(false);

                // Lógica si no hay resultados
                if (customers.isEmpty()) {
                    if (manualRefresh || mIsFirstLoad) {
                        mView.showEmptyState();
                    } else {
                        mView.showLoadMoreIndicator(false);
                    }
                    mView.showEndlessScroll(false);
                } else {
                    if (manualRefresh || mIsFirstLoad) {
                        mView.showCustomers(customers);
                    } else {
                        mView.showLoadMoreIndicator(false);
                        mView.showCustomersPage(customers);
                    }
                    mView.showEndlessScroll(true);
                }

                mIsFirstLoad = false; // Off
            }

            @Override
            public void onError(String error) {
                mView.showLoadingState(false);
                mView.showLoadMoreIndicator(false);
                mView.showCustomersError(error);
            }
        });
    }

}
