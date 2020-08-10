package nanifarfalla.app.addeditinvoice.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nanifarfalla.app.R;
import nanifarfalla.app.addeditinvoice.AddEditInvoiceActivity;
import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;
import nanifarfalla.app.addeditinvoiceitem.AddEditInvoiceItemActivity;
import nanifarfalla.app.customers.CustomersActivity;
import nanifarfalla.app.util.DateTimeUtils;

//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.RecyclerView;

/**
 * Vista para "Añadir factura"
 */
public class AddEditInvoiceFragment extends Fragment implements AddEditInvoiceMvp.View {

    // Keys de estados
    private static final String BUNDLE_CUSTOMER_ID = "BUNDLE_CUSTOMER_ID";

    private AddEditInvoiceMvp.Presenter mPresenter;

    private TextView mCustomerField;
    private TextView mDateField;
    private Button mAddItemButton;
    private RecyclerView mInvoiceItemsList;
    private InvoiceItemAdapter mInvoiceItemAdapter;

    private TextView mSubtotalText;
    private TextView mTaxText;
    private TextView mTotalText;
    private TextInputLayout mAddItemButtonWrapper;
    private TextInputLayout mCustomerFieldWrapper;

    private String mCustomerId;
    private Date mDate;


    public AddEditInvoiceFragment() {
        // Required empty public constructor
    }

    public static AddEditInvoiceFragment newInstance() {
        AddEditInvoiceFragment fragment = new AddEditInvoiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddEditInvoiceActivity.REQUEST_PICK_CUSTOMER:
                if (Activity.RESULT_OK == resultCode) {
                    mCustomerId = data.getStringExtra(CustomersActivity.EXTRA_CUSTOMER_ID);
                    mPresenter.manageCustomerPickingResult(mCustomerId);
                }
                break;
            case AddEditInvoiceActivity.REQUEST_ADD_INVOICE_ITEM:
                if (Activity.RESULT_OK == resultCode) {
                    mPresenter.manageAdditionResult();
                }
                break;
            case AddEditInvoiceActivity.REQUEST_EDIT_INVOICE_ITEM:
                if (Activity.RESULT_OK == resultCode) {
                    mPresenter.manageEditionResult();
                }
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_invoice, container, false);

        // Cliente
        mCustomerFieldWrapper =  root.findViewById(R.id.customer_text_input);
        mCustomerField = (TextView) root.findViewById(R.id.customer_field);
        mCustomerField.setOnClickListener( view -> mPresenter.selectCustomer() );

        // Fecha
        mDateField = (TextView) root.findViewById(R.id.invoice_date_field);
        mDate = DateTimeUtils.getTime();
        String currentDateString = DateTimeUtils.formatDate(mDate, DateTimeUtils.DATE_ONLY_PATTERN);
        mDateField.setText(currentDateString);

        // Botón ADD
        mAddItemButtonWrapper =  root.findViewById(R.id.add_item_button_wrapper);
        mAddItemButton = (Button) root.findViewById(R.id.add_item_button);
        mAddItemButton.setOnClickListener( view -> mPresenter.addNewInvoiceItem() );

        // Items
        mInvoiceItemsList = root.findViewById(R.id.invoice_items_list);
        DividerItemDecoration decoration = new DividerItemDecoration(mInvoiceItemsList.getContext(),
                DividerItemDecoration.VERTICAL);
        mInvoiceItemAdapter = new InvoiceItemAdapter(getContext(),
                new ArrayList<>( 0 ),
                new InvoiceItemAdapter.ItemListener() {
                    @Override
                    public void onItemClick(InvoiceItemUi invoiceItem) {
                        mPresenter.editInvoiceItem(invoiceItem.getProductId());
                    }

                    @Override
                    public void onRemoveItemClick(InvoiceItemUi invoiceItem) {
                        mPresenter.deleteInvoiceItem(invoiceItem.getProductId());
                    }
                });
        mInvoiceItemsList.setAdapter(mInvoiceItemAdapter);

        mInvoiceItemsList.addItemDecoration(decoration);

        // Importes totales
        mSubtotalText = (TextView) root.findViewById(R.id.subtotal_value);
        mTaxText = (TextView) root.findViewById(R.id.tax_value);
        mTotalText = (TextView) root.findViewById(R.id.total_value);
        mSubtotalText.setText("$0");
        mTaxText.setText("$0");
        mTotalText.setText("$0");

        // Mas campos...

        // Restablecer estado anterior
        if (savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_CUSTOMER_ID)) {
            mCustomerId = savedInstanceState.getString(BUNDLE_CUSTOMER_ID);
            mPresenter.restoreState(mCustomerId);
        }

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadInvoiceItems();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCustomerId != null) {
            outState.putString(BUNDLE_CUSTOMER_ID, mCustomerId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_invoice) {
            mPresenter.saveInvoice(
                    mCustomerId,
                    mDate
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCustomer(String name) {
        mCustomerFieldWrapper.setErrorEnabled(false);
        mCustomerField.setText(name);
    }

    @Override
    public void showCustomerError() {
        mCustomerFieldWrapper.setErrorEnabled(true);
        mCustomerFieldWrapper.setError("El cliente es requerido");
        requestViewFocus(mCustomerFieldWrapper);
    }

    @Override
    public void showItemsError() {
        mAddItemButtonWrapper.setErrorEnabled(true);
        mAddItemButtonWrapper.setError("Al menos un item es requerido");
        requestViewFocus(mAddItemButtonWrapper);
    }

    @Override
    public void showInvoicesScreen(String invoiceId) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showSaveError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCustomersScreen() {
        Intent intent = new Intent(getActivity(), CustomersActivity.class);
        intent.setAction(CustomersActivity.ACTION_PICK_CUSTOMER);
        startActivityForResult(intent, AddEditInvoiceActivity.REQUEST_PICK_CUSTOMER);
    }

    @Override
    public void showInvoiceItems(List<InvoiceItemUi> invoiceItemUis) {
        mInvoiceItemAdapter.replaceData(invoiceItemUis);
        mAddItemButtonWrapper.setErrorEnabled(false);
    }

    @Override
    public void showAddInvoiceItemScreen() {
        Intent requestIntent = new Intent(getActivity()
                , AddEditInvoiceItemActivity.class);
        startActivityForResult(requestIntent
                , AddEditInvoiceActivity.REQUEST_ADD_INVOICE_ITEM);
    }

    @Override
    public void showEditInvoiceItemScreen(@NonNull String productId) {
        Intent requestIntent = new Intent(getContext(), AddEditInvoiceItemActivity.class);
        requestIntent.putExtra(AddEditInvoiceActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(requestIntent, AddEditInvoiceActivity.REQUEST_EDIT_INVOICE_ITEM);
    }

    @Override
    public void showInvoiceAmounts(String subtotal, String tax, String total) {
        mSubtotalText.setText(subtotal);
        mTaxText.setText(tax);
        mTotalText.setText(total);
    }

    @Override
    public void setPresenter(AddEditInvoiceMvp.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }

    private void requestViewFocus(View view) {
        ViewParent parent = view.getParent();
        parent.requestChildFocus(view, view);
    }
}
