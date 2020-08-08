package nanifarfalla.app.products.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.hermosaprogramacion.premium.appproductos.R;
import com.hermosaprogramacion.premium.appproductos.login.LoginActivity;
import com.hermosaprogramacion.premium.appproductos.productdetail.ProductDetailActivity;
import com.hermosaprogramacion.premium.appproductos.products.ProductsActivity;
import com.hermosaprogramacion.premium.appproductos.products.domain.model.Product;
import com.hermosaprogramacion.premium.appproductos.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para mostrar la lista de productos
 */
public class ProductsFragment extends Fragment implements ProductsMvp.View {

    private static final String ARGUMENT_ACTION_PICK = "ARGUMENT_ACTION_PICK";

    // Referencias UI
    private RecyclerView mProductsList;
    private ProductsAdapter mProductsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mEmptyView;

    private ProductsAdapter.ProductItemListener mItemListener =
            new ProductsAdapter.ProductItemListener() {
                @Override
                public void onProductClick(Product clickedProduct) {
                    FragmentActivity activity = getActivity();
                    if (ProductsActivity.ACTION_PICK_PRODUCT
                            .equals(activity.getIntent().getAction())) {
                        showAddEditInvoiceItemScreen(clickedProduct);
                    } else {
                        mProductsPresenter.openProductDetails(clickedProduct.getCode());
                    }
                }
            };



    // Relaciones de composición
    private ProductsMvp.Presenter mProductsPresenter;

    private boolean mActionPick;


    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance(boolean actionPick) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARGUMENT_ACTION_PICK, actionPick);
        ProductsFragment productsFragment = new ProductsFragment();
        productsFragment.setArguments(arguments);
        return productsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductsAdapter = new ProductsAdapter(new ArrayList<Product>(0), mItemListener);

        mActionPick = getArguments().getBoolean(ARGUMENT_ACTION_PICK);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // En caso de estar en acción de selección, se evita inflar fragment_products (w900dp)
        int layoutId = mActionPick ? R.layout.fragment_products_swipe : R.layout.fragment_products;
        View root = inflater.inflate(layoutId, container, false);

        if (ActivityUtils.isTwoPane(getActivity()) && !mActionPick) {
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbarList);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        mProductsList = (RecyclerView) root.findViewById(R.id.products_list);
        mEmptyView = root.findViewById(R.id.noProducts);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        // Setup
        setUpProductsList();
        setUptRefreshLayout();

        if (savedInstanceState != null) {
            hideList(false);
        }

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mProductsPresenter.loadProducts(false);
    }

    private void setUpProductsList() {
        mProductsList.setAdapter(mProductsAdapter);

        final LinearLayoutManager layoutManager =
                (LinearLayoutManager) mProductsList.getLayoutManager();

        // Se agrega escucha de scroll infinito.
        mProductsList.addOnScrollListener(
                new InfinityScrollListener(mProductsAdapter, layoutManager) {
                    @Override
                    public void onLoadMore() {
                        mProductsPresenter.loadProducts(false);
                    }
                });
    }

    private void setUptRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProductsPresenter.loadProducts(true);
            }
        });
    }


    @Override
    public void showProducts(List<Product> products) {
        mProductsAdapter.replaceData(products);
        hideList(false);
    }

    @Override
    public void showProductsPage(List<Product> products) {
        mProductsAdapter.addData(products);
        hideList(false);
    }

    @Override
    public void showLoadingState(final boolean show) {
        if (getView() == null) {
            return;
        }

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public void showEmptyState() {
        hideList(true);
    }

    private void hideList(boolean hide) {
        mProductsList.setVisibility(hide ? View.GONE : View.VISIBLE);
        mEmptyView.setVisibility(hide ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProductsError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoadMoreIndicator(boolean show) {
        if (!show) {
            mProductsAdapter.dataFinishedLoading();
        } else {
            mProductsAdapter.dataStartedLoading();
        }
    }

    @Override
    public void allowMoreData(boolean allow) {
        mProductsAdapter.setMoreData(allow);
    }

    @Override
    public void setPresenter(ProductsMvp.Presenter presenter) {
        mProductsPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    public ProductsMvp.Presenter getPresenter() {
        return mProductsPresenter;
    }

    @Override
    public void showLoginScreen() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void showProductDetailScreen(String productCode) {
        Intent i = new Intent(getActivity(), ProductDetailActivity.class);
        i.putExtra(ProductDetailActivity.EXTRA_PRODUCT_CODE, productCode);
        startActivity(i);
    }

    private void showAddEditInvoiceItemScreen(Product clickedProduct) {
        Intent responseIntent = new Intent();
        responseIntent.putExtra(ProductsActivity.EXTRA_PRODUCT_ID, clickedProduct.getCode());
        getActivity().setResult(Activity.RESULT_OK, responseIntent);
        getActivity().finish();
    }
}
