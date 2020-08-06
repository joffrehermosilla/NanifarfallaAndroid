package nanifarfalla.app.customers;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.hermosaprogramacion.premium.appproductos.BaseActivity;
import com.hermosaprogramacion.premium.appproductos.R;
import com.hermosaprogramacion.premium.appproductos.customers.presentation.CustomersFragment;
import com.hermosaprogramacion.premium.appproductos.customers.presentation.CustomersPresenter;
import com.hermosaprogramacion.premium.appproductos.di.DependencyProvider;

public class CustomersActivity extends BaseActivity {

    // Acción de selección de clientes
    public static final String ACTION_PICK_CUSTOMER
            = "com.hermosaprogramacion.action.ACTION_PICK_CUSTOMER";
    // Extras
    public static final String EXTRA_CUSTOMER_ID = "com.hermosaprogramacion.EXTRA_CUSTOMER_ID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        if(isActionPick()){
            setTitle(R.string.pick_customer);
        }

        // Obtener fragmento por si ya está instalado
        CustomersFragment fragment = (CustomersFragment)
                getSupportFragmentManager().findFragmentById(R.id.customers_container);

        // Añadir fragmento si no existe aún
        if (fragment == null) {
            fragment = CustomersFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.customers_container, fragment)
                    .commit();
        }

        // Instancear presentador
        CustomersPresenter presenter = new CustomersPresenter(
                fragment,
                DependencyProvider.provideGetCustomers());

        // Satisfacer dependencia del presentador en el fragmento (vista)
        fragment.setPresenter(presenter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customers_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showActionPickNavigation();
    }

    private boolean isActionPick(){
        return ACTION_PICK_CUSTOMER.equals(getIntent().getAction());
    }

    private void showActionPickNavigation() {
        if (isActionPick()) {
            setToolbarAsUp(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected NavDrawerItemEnum getNavDrawerItem() {
        return NavDrawerItemEnum.CUSTOMERS;
    }
}
