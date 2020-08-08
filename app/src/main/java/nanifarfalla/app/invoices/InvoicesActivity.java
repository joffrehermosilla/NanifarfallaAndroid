package nanifarfalla.app.invoices;

import android.os.Bundle;
import android.view.Menu;

import nanifarfalla.app.BaseActivity;
import nanifarfalla.app.R;
import nanifarfalla.app.di.DependencyProvider;
import nanifarfalla.app.invoices.presentation.InvoicesFragment;
import nanifarfalla.app.invoices.presentation.InvoicesPresenter;

public class InvoicesActivity extends BaseActivity{

    public static final int REQUEST_ADD_INVOICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        InvoicesFragment fragment = (InvoicesFragment)
                getSupportFragmentManager().findFragmentById(R.id.invoices_container);

        setTitle(R.string.invoices);

        if (fragment == null) {
            fragment = InvoicesFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.invoices_container, fragment)
                    .commit();
        }

        InvoicesPresenter presenter = new InvoicesPresenter(
                fragment,
                DependencyProvider.provideGetInvoices());

        fragment.setPresenter(presenter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invoices_menu, menu);
        return true;
    }


    @Override
    protected NavDrawerItemEnum getNavDrawerItem(){
        return NavDrawerItemEnum.INVOICES;
    }
}
