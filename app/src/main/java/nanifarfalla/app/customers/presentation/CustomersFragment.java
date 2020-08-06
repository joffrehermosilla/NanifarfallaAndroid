package nanifarfalla.app.customers.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hermosaprogramacion.premium.appproductos.R;
import com.hermosaprogramacion.premium.appproductos.customers.CustomersActivity;
import com.hermosaprogramacion.premium.appproductos.customers.domain.entities.Customer;
import com.hermosaprogramacion.premium.appproductos.customers.presentation.CustomersAdapter.CustomerItemListener;
import com.hermosaprogramacion.premium.appproductos.products.presentation.InfinityScrollListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación de la vista para la lista de clientes
 */
public class CustomersFragment extends Fragment implements CustomersListMvp.View {

    private SwipeRefreshLayout mSwipeToRefreshView;
    private RecyclerView mCustomersList;
    private CustomersAdapter mCustomersAdapter;
    private View mNoCustomersView;

    private CustomersListMvp.Presenter mCustomersPresenter;


    public CustomersFragment() {

    }

    public static CustomersFragment newInstance() {
        CustomersFragment fragment = new CustomersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCustomersAdapter = new CustomersAdapter(new ArrayList<Customer>(0),
                new CustomerItemListener() {
                    @Override
                    public void onCustomerClick(Customer clickedCustomer) {

                        if (CustomersActivity.ACTION_PICK_CUSTOMER.equals(
                                getActivity().getIntent().getAction())) {
                            showAddEditInvoiceScreen(clickedCustomer.getId());
                        }
                    }
                });


        setHasOptionsMenu(true);
    }

    private void showAddEditInvoiceScreen(String customerId) {
        Intent intent = new Intent();
        intent.putExtra(CustomersActivity.EXTRA_CUSTOMER_ID,customerId);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);

        mSwipeToRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_to_refresh_view);
        mCustomersList = (RecyclerView) rootView.findViewById(R.id.customers_list);
        mNoCustomersView = rootView.findViewById(R.id.no_customers_view);

        setUpCustomersList();
        setUpRefreshView();

        if (savedInstanceState != null) {
            showList(true);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCustomersPresenter.loadCustomers(false);
    }

    private void setUpCustomersList() {
        mCustomersList.setAdapter(mCustomersAdapter);

        final LinearLayoutManager layoutManager =
                (LinearLayoutManager) mCustomersList.getLayoutManager();

        // Se agrega escucha de scroll infinito
        mCustomersList.addOnScrollListener(
                new InfinityScrollListener(mCustomersAdapter, layoutManager) {
                    @Override
                    public void onLoadMore() {
                        mCustomersPresenter.loadCustomers(false);
                    }
                });
    }

    private void setUpRefreshView() {
        mSwipeToRefreshView.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        mSwipeToRefreshView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mCustomersPresenter.loadCustomers(true);
                    }
                });
    }

    @Override
    public void showCustomers(List<Customer> customers) {
        mCustomersAdapter.replaceItems(customers);
        showList(true);
    }

    private void showList(boolean show) {
        mCustomersList.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoCustomersView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showLoadingState(final boolean show) {
        if (getView() == null) {
            return;
        }

        mSwipeToRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToRefreshView.setRefreshing(show);
            }
        });
    }

    @Override
    public void showEmptyState() {
        showList(false);
    }

    @Override
    public void showCustomersError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showCustomersPage(List<Customer> customers) {
        mCustomersAdapter.addItems(customers);
        showList(true);
    }

    @Override
    public void showLoadMoreIndicator(boolean show) {
        if (show) {
            mCustomersAdapter.dataStartedLoading();
        } else {
            mCustomersAdapter.dataFinishedLoading();
        }
    }

    @Override
    public void showEndlessScroll(boolean show) {
        mCustomersAdapter.setEndless(show);
    }

    @Override
    public void setPresenter(CustomersListMvp.Presenter presenter) {
        mCustomersPresenter = checkNotNull(presenter, "presenter no puede ser null");
    }
}
