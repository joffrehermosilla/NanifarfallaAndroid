package nanifarfalla.app.customers.presentation;

import java.util.List;

import nanifarfalla.app.customers.domain.entities.Customer;

/**
 * Contrato MVP para la lista de clientes
 */

public interface CustomersListMvp {
    interface View {
        void showCustomers(List<Customer> customers);

        void showLoadingState(boolean show);

        void showEmptyState();

        void showCustomersError(String msg);

        void showCustomersPage(List<Customer> customers);

        void showLoadMoreIndicator(boolean show);

        void showEndlessScroll(boolean show);

        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void loadCustomers(boolean refresh);

    }
}
