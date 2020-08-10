package nanifarfalla.app.addeditinvoiceitem.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Preconditions;

import nanifarfalla.app.R;
import nanifarfalla.app.addeditinvoiceitem.AddEditInvoiceItemActivity;
import nanifarfalla.app.products.ProductsActivity;

//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.Fragment;

/**
 * Fragmento para "Añadir/Editar item de factura"
 */
public class AddEditInvoiceItemFragment extends Fragment implements AddEditInvoiceItemMvp.View {

    public static final String BUNDLE_PRODUCT_ID = "BUNDLE_PRODUCT_ID";

    private AddEditInvoiceItemMvp.Presenter mPresenter;

    private TextInputLayout mProductFieldWrapper;
    private TextView mProductField;
    private TextInputLayout mQuantityFieldWrapper;
    private EditText mQuantityField;
    private TextView mStockView;

    private String mSelectedProductId;

    public AddEditInvoiceItemFragment() {
        // Required empty public constructor
    }

    public static AddEditInvoiceItemFragment newInstance() {
        AddEditInvoiceItemFragment fragment = new AddEditInvoiceItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_invoice_item,
                container, false);

        mProductFieldWrapper = (TextInputLayout) root.findViewById(R.id.product_selection_field_wrapper);

        mProductField = (TextView) root.findViewById(R.id.product_selection_field);
        mProductField.setOnClickListener( view -> mPresenter.selectProduct() );

        mQuantityFieldWrapper = (TextInputLayout) root.findViewById(R.id.quantity_field_wrapper);

        mQuantityField = (EditText) root.findViewById(R.id.quantity_field);

        mStockView = (TextView) root.findViewById(R.id.stock_text);

        // Restaurar estado
        if (savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_PRODUCT_ID)) {
            mSelectedProductId = savedInstanceState.getString(BUNDLE_PRODUCT_ID);
            mPresenter.restoreState(mSelectedProductId);
        }

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.populateInvoiceItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSelectedProductId != null) {
            outState.putString(BUNDLE_PRODUCT_ID, mSelectedProductId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddEditInvoiceItemActivity.REQUEST_PICK_PRODUCT == requestCode
                && Activity.RESULT_OK == resultCode) {

            mSelectedProductId = data.getStringExtra( ProductsActivity.EXTRA_PRODUCT_ID);
            mPresenter.manageProductPickingResult(mSelectedProductId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_save_invoice_item == item.getItemId()) {
            mPresenter.saveInvoiceItem(
                    mSelectedProductId,
                    mProductField.getText().toString(),
                    mQuantityField.getText().toString()
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProductName(String productName) {
        mProductFieldWrapper.setErrorEnabled(false);
        mProductField.setText(productName);
    }

    @Override
    public void showQuantity(String quantity) {
        mQuantityFieldWrapper.setErrorEnabled(false);
        mQuantityField.setText(quantity);
    }

    @Override
    public void showStock(String stock) {
        mStockView.setText(stock);
    }

    @Override
    public void showProductNotSelectedError() {
        mProductFieldWrapper.setErrorEnabled(true);
        mProductFieldWrapper.setError(getString(R.string.error_select_product));
        requestViewFocus(mProductFieldWrapper);
    }

    @Override
    public void showQuantityError() {
        mQuantityFieldWrapper.setErrorEnabled(true);
        mQuantityFieldWrapper.setError(getString(R.string.error_quantity));
        requestViewFocus(mQuantityFieldWrapper);
    }

    @Override
    public void showProductsScreen() {
        Intent requestIntent = new Intent(getActivity(), ProductsActivity.class);
        requestIntent.setAction(ProductsActivity.ACTION_PICK_PRODUCT);
        startActivityForResult(requestIntent, AddEditInvoiceItemActivity.REQUEST_PICK_PRODUCT);
    }

    @Override
    public void showAddEditInvoiceScreen() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showMissingInvoiceItem() {
        mProductField.setText(R.string.no_data);
        mQuantityField.setText("0");
    }

    @Override
    public void showMissingProduct() {
        mProductField.setText(R.string.no_data);
    }

    @Override
    public void setPresenter(AddEditInvoiceItemMvp.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }

    private void requestViewFocus(View view) {
        ViewParent parent = view.getParent();
        parent.requestChildFocus(view, view);
    }
}
