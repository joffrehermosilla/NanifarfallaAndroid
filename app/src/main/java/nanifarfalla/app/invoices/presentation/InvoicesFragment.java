package nanifarfalla.app.invoices.presentation;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.hermosaprogramacion.premium.appproductos.addeditinvoice.AddEditInvoiceActivity;
import com.hermosaprogramacion.premium.appproductos.invoices.InvoicesActivity;
import com.hermosaprogramacion.premium.appproductos.invoices.domain.entities.InvoiceUi;
import com.hermosaprogramacion.premium.appproductos.products.presentation.InfinityScrollListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Fragmento de lista de facturas
 */
public class InvoicesFragment extends Fragment implements InvoicesListMvp.View {

    private InvoicesListMvp.Presenter mInvoicesPresenter;

    private SwipeRefreshLayout mSwipeToRefreshView;
    private RecyclerView mInvoicesList;
    private InvoicesAdapter mInvoicesAdapter;
    private View mNoInvoicesView;

    public InvoicesFragment() {
    }


    public static InvoicesFragment newInstance() {
        InvoicesFragment fragment = new InvoicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInvoicesAdapter = new InvoicesAdapter(getContext(),
                new ArrayList<InvoiceUi>(0));

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mInvoicesPresenter.manageSavingResult(requestCode, resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoices, container, false);

        mSwipeToRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_to_refresh_view);
        mInvoicesList = (RecyclerView) rootView.findViewById(R.id.invoices_list);
        mNoInvoicesView = rootView.findViewById(R.id.no_invoices_view);

        setUpInvoicesList();
        setUpRefreshView();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoicesPresenter.addNewInvoice();
            }
        });

        if (savedInstanceState != null) {
            showList(true);
        }
        return rootView;
    }

    private void setUpInvoicesList() {
        mInvoicesList.setAdapter(mInvoicesAdapter);

        final LinearLayoutManager layoutManager =
                (LinearLayoutManager) mInvoicesList.getLayoutManager();

        // Se agrega escucha de scroll infinito
        mInvoicesList.addOnScrollListener(
                new InfinityScrollListener(mInvoicesAdapter, layoutManager) {
                    @Override
                    public void onLoadMore() {
                        mInvoicesPresenter.loadInvoices(false, false);
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
                        mInvoicesPresenter.loadInvoices(true, false);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mInvoicesPresenter.loadInvoices(false, true);
    }

    @Override
    public void showInvoices(List<InvoiceUi> invoices) {
        mInvoicesAdapter.replaceItems(invoices);
        showList(true);
    }

    @Override
    public void showAddInvoice() {
        Intent intent = new Intent(getContext(), AddEditInvoiceActivity.class);
        startActivityForResult(intent, InvoicesActivity.REQUEST_ADD_INVOICE);
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
    public void showInvoicesError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showInvoicesPage(List<InvoiceUi> invoices) {
        mInvoicesAdapter.addItems(invoices);
        showList(true);
    }

    @Override
    public void showLoadMoreIndicator(boolean show) {
        if (!show) {
            mInvoicesAdapter.dataFinishedLoading();
        } else {
            mInvoicesAdapter.dataStartedLoading();
        }
    }

    @Override
    public void showEndlessScroll(boolean show) {
        mInvoicesAdapter.setEndless(show);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                R.string.message_successfully_saved_invoice,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(InvoicesListMvp.Presenter presenter) {
        mInvoicesPresenter = checkNotNull(presenter, "presenter no puede ser null");
    }

    private void showList(boolean show) {
        mInvoicesList.setVisibility(show ? View.VISIBLE : View.GONE);
        mNoInvoicesView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
