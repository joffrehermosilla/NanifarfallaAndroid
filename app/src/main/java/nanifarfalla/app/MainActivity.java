package nanifarfalla.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import nanifarfalla.app.customers.CustomersActivity;
import nanifarfalla.app.invoices.InvoicesActivity;
import nanifarfalla.app.login.LoginActivity;
import nanifarfalla.app.login.data.preferences.UserPrefs;
import nanifarfalla.app.products.ProductsActivity;
import nanifarfalla.app.rxjava.RX00IntroActivity;
import nanifarfalla.app.rxjava.RX01DisposableActivity;
import nanifarfalla.app.rxjava.RX02CompositeDisposableActivity;
import nanifarfalla.app.rxjava.RX03OperadoresActivity;
import nanifarfalla.app.rxjava.RX04TiposObservablesActivity;
import nanifarfalla.app.rxjava.RX05SubjectActivity;
import nanifarfalla.app.rxjava.RX06BusActivity;
import nanifarfalla.app.rxjava.RX07BindingActivity;
import nanifarfalla.app.rxjava.RX08BackPressureActivity;
import nanifarfalla.app.rxjava.RX09HotAndColdActivity;

public class MainActivity extends AppCompatActivity {
    private Button btRX00Introduccion;
    private Button btRX01Disposable;
    private Button btRX02CompositeDisposable;
    private Button btRX03Operadores;
    private Button btRX04TipoObservables;
    private Button btRX05Subject;
    private Button btRX06Bus;
    private Button btRX07Binding;
    private Button btRX08BackPressure;
    private Button btRX09HotAndCold;




    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private View mNavHeader;

    public enum NavDrawerItemEnum {
        INVOICES(R.id.invoices_nav_item),
        PRODUCTS(R.id.products_nav_item),
        CUSTOMERS(R.id.customers_nav_item),
        INVALID(0);

        private int id;

        NavDrawerItemEnum(int navItemId) {
            id = navItemId;
        }

        public int getId() {
            return id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Controlar otras características compartidas adicionales
        setContentView(R.layout.activity_main);

        setUpView();



        // Redirección al Login
        if (!UserPrefs.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar(R.id.toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        // Inflar cabecera del nav drawer
        mNavHeader = mNavigationView.inflateHeaderView(R.layout.nav_header);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        onNavDrawerItemClicked(item);
                        return true;
                    }
                });

        setNavDrawerCheckedItem();
        setupUserBox();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Setear toolbar para dos paneles
        getToolbar(R.id.toolbarList);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void setNavDrawerCheckedItem() {
        mNavigationView.setCheckedItem(getNavDrawerItem().getId());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen( GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    public Toolbar getToolbar(int toolbar) {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        }
        return mToolbar;
    }

    protected void setToolbarAsUp(View.OnClickListener clickListener) {
        if (mDrawerToggle == null) {
            return;
        }
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setToolbarNavigationClickListener(clickListener);
    }

    protected NavDrawerItemEnum getNavDrawerItem() {
        return MainActivity.NavDrawerItemEnum.INVALID;
    }

    private void setupUserBox() {
        // TODO: Poblar más views, agregar más acciones

        // Poner email
        TextView userNameView = (TextView) mNavHeader.findViewById(R.id.salesman_name);
        String userName = UserPrefs.getInstance(this).getUserName();
        userNameView.setText(userName);
    }

    private void onNavDrawerItemClicked(MenuItem item) {
        // Marcamos la sección
        mNavigationView.setCheckedItem(item.getItemId());

        // Procesamos el comportamiento según la sección elegida
        itemSelected(item.getItemId());

        // Cerramos el drawer
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    private void itemSelected(int navDrawerItemId) {
        switch (navDrawerItemId) {
            case R.id.invoices_nav_item:
                launchSectionActivity( InvoicesActivity.class);
                break;
            case R.id.products_nav_item:
                launchSectionActivity( ProductsActivity.class);
                break;
            case R.id.customers_nav_item:
                launchSectionActivity( CustomersActivity.class);
                break;
            case R.id.settings_nav_item:
                // TODO: Ejecutar tu actividad de ajustes
                Snackbar.make(findViewById(android.R.id.content),
                        "Ajustes", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.log_out_nav_item:
                // Cerrar sesión
                logOut();
                break;
        }
    }

    private void launchSectionActivity(Class activityToLaunch) {
        Intent intent = new Intent(this, activityToLaunch);
        startActivity(intent);
        finish();
    }

    private void logOut() {
        UserPrefs.getInstance(getApplicationContext()).delete();
        launchSectionActivity(LoginActivity.class);
    }


    private void setUpView(){
        btRX00Introduccion = findViewById(R.id.btRX00Introduccion);
        btRX00Introduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX00IntroActivity.class));
            }
        });
        btRX01Disposable = findViewById(R.id.btRX01Disposable);
        btRX01Disposable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX01DisposableActivity.class));
            }
        });
        btRX02CompositeDisposable = findViewById(R.id.btRX02CompositeDisposable);
        btRX02CompositeDisposable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX02CompositeDisposableActivity.class));
            }
        });

        btRX03Operadores = findViewById(R.id.btRX03Operadores);
        btRX03Operadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX03OperadoresActivity.class));
            }
        });

        btRX04TipoObservables = findViewById(R.id.btRX04TipoObservables);
        btRX04TipoObservables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX04TiposObservablesActivity.class));
            }
        });

        btRX05Subject = findViewById(R.id.btRX05Subject);
        btRX05Subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX05SubjectActivity.class));
            }
        });

        btRX06Bus = findViewById(R.id.btRX06Bus);
        btRX06Bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX06BusActivity.class));
            }
        });

        btRX07Binding = findViewById(R.id.btRX07Binding);
        btRX07Binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX07BindingActivity.class));
            }
        });

        btRX08BackPressure = findViewById(R.id.btRX08BackPressure);
        btRX08BackPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX08BackPressureActivity.class));
            }
        });

        btRX09HotAndCold = findViewById(R.id.btRX09HotAndCold);
        btRX09HotAndCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RX09HotAndColdActivity.class));
            }
        });
    }

}
