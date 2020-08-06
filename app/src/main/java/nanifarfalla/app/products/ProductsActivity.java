package nanifarfalla.app.products;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import nanifarfalla.app.R;

//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

public class ProductsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProductsFragment mProductsFragment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        // Referencias UI
        mToolbar = (Toolbar) findViewById( R.id.toolbar);
        mProductsFragment = (ProductsFragment) getSupportFragmentManager().findFragmentById( R.id.products_container);

        // Setup
        setUpToolbar();
        setUpProductsFragment();


    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setSupportActionBar(Toolbar mToolbar) {
    }

    private void setUpProductsFragment() {
        if (mProductsFragment == null) {
            mProductsFragment = ProductsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add( R.id.products_container, mProductsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
