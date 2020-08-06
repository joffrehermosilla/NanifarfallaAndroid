package nanifarfalla.app.addeditinvoice;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.hermosaprogramacion.premium.appproductos.R;
import com.hermosaprogramacion.premium.appproductos.addeditinvoice.presentation.AddEditInvoiceFragment;
import com.hermosaprogramacion.premium.appproductos.addeditinvoice.presentation.AddEditInvoicePresenter;
import com.hermosaprogramacion.premium.appproductos.addeditinvoice.presentation.DiscardChangesDialog;
import com.hermosaprogramacion.premium.appproductos.di.DependencyProvider;

public class AddEditInvoiceActivity extends AppCompatActivity
        implements DiscardChangesDialog.DiscardDialogListener {

    public static final int REQUEST_ADD_INVOICE_ITEM = 1;
    public static final int REQUEST_PICK_CUSTOMER = 2;
    public static final int REQUEST_EDIT_INVOICE_ITEM = 3;

    public static final String EXTRA_PRODUCT_ID
            = "com.hermosaprogramacion.EXTRA_PRODUCT_ID";

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddEditInvoiceFragment view = (AddEditInvoiceFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_edit_invoice_container);
        if (view == null) {
            view = AddEditInvoiceFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_invoice_container, view)
                    .commit();
        }

        String invoiceId= getIntent().getStringExtra("");

        setToolbarTitle(invoiceId);

        AddEditInvoicePresenter presenter = new AddEditInvoicePresenter(
                view,
                DependencyProvider.provideGetCustomers(),
                DependencyProvider.provideSaveInvoice(),
                DependencyProvider.provideCacheInvoiceItemsStore(this)
        );

        view.setPresenter(presenter);
    }

    private void setToolbarTitle(String invoiceId) {
        if(invoiceId==null){
            setTitle(R.string.add_invoice);
        }else {
            setTitle(R.string.edit_invoice);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_invoice_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO: Crea un método o interfaz en el fragmento para determinar si hubo cambios
        DiscardChangesDialog dialog = new DiscardChangesDialog();
        dialog.show(getSupportFragmentManager(), "DiscardDialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        resetCache();
        finish();
    }



    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void resetCache() {
        // Limpiar caché de items de factura
        DependencyProvider.provideCacheInvoiceItemsStore(this).deleteAll();
    }
}
