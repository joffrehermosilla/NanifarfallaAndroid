package nanifarfalla.app.addeditinvoiceitem;

import android.os.Bundle;
//import android.support.v7.app.ActionBar;
import androidx.appcompat.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import nanifarfalla.app.R;
import nanifarfalla.app.addeditinvoice.AddEditInvoiceActivity;
import nanifarfalla.app.addeditinvoiceitem.presentation.AddEditInvoiceItemFragment;
import nanifarfalla.app.addeditinvoiceitem.presentation.AddEditInvoiceItemPresenter;
import nanifarfalla.app.di.DependencyProvider;

public class AddEditInvoiceItemActivity extends AppCompatActivity {

    public final static int REQUEST_PICK_PRODUCT = 1;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_invoice_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        AddEditInvoiceItemFragment view = (AddEditInvoiceItemFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_edit_invoice_item_container);

        if (view == null) {
            view = AddEditInvoiceItemFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_invoice_item_container, view)
                    .commit();
        }

        String productId = getIntent().getStringExtra(
                AddEditInvoiceActivity.EXTRA_PRODUCT_ID);

        // Cambiar título de la actividad
        if (productId == null) {
            setTitle(R.string.add_invoice_item);
        } else {
            setTitle(R.string.edit_invoice_item);
        }

        AddEditInvoiceItemPresenter presenter =
                new AddEditInvoiceItemPresenter(
                        productId,
                        view,
                        DependencyProvider.provideGetProducts(this),
                        DependencyProvider.provideCacheInvoiceItemsStore(this),
                        getResources());
        view.setPresenter(presenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_invoice_item_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
