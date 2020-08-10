package nanifarfalla.app.di;

import android.content.Context;

import androidx.annotation.NonNull;

import nanifarfalla.app.addeditinvoiceitem.data.CacheInvoiceItemsStore;
import nanifarfalla.app.customers.data.CustomersRepository;
import nanifarfalla.app.customers.data.cache.CacheCustomersStore;
import nanifarfalla.app.customers.domain.usecases.GetCustomers;
import nanifarfalla.app.invoices.data.InvoicesRepository;
import nanifarfalla.app.invoices.data.cache.CacheInvoicesStore;
import nanifarfalla.app.invoices.domain.usecases.GetInvoicesForUi;
import nanifarfalla.app.invoices.domain.usecases.SaveInvoice;
import nanifarfalla.app.login.data.UsersRepository;
import nanifarfalla.app.login.data.cloud.CloudUsersDataSource;
import nanifarfalla.app.login.data.preferences.UserPrefs;
import nanifarfalla.app.login.domain.usecases.LoginInteractor;
import nanifarfalla.app.products.data.ProductsRepository;
import nanifarfalla.app.products.data.datasource.cloud.CloudProductsDataSource;
import nanifarfalla.app.products.data.datasource.local.LocalProductsDataSource;
import nanifarfalla.app.products.data.datasource.memory.MemoryProductsDataSource;
import nanifarfalla.app.products.domain.usecases.GetProducts;

//import android.support.annotation.NonNull;

/**
 * Contenedor de dependencias
 */
public final class DependencyProvider {

    private DependencyProvider() {
    }

    public static ProductsRepository provideProductsRepository(@NonNull Context context) {
        return ProductsRepository.getInstance(
                MemoryProductsDataSource.getInstance(),
                LocalProductsDataSource.getInstance(context.getContentResolver()),
                CloudProductsDataSource.getInstance(),
                UserPrefs.getInstance(context),
                context);
    }

    public static UsersRepository provideUsersRepository(@NonNull Context context) {
        return UsersRepository.getInstance(CloudUsersDataSource.getInstance(),
                UserPrefs.getInstance(context), context);
    }

    public static LoginInteractor provideLoginInteractor(@NonNull Context context) {
        return new LoginInteractor(provideUsersRepository(context));
    }

    public static GetProducts provideGetProducts(@NonNull Context context) {
        return new GetProducts(provideProductsRepository(context));
    }

    public static GetCustomers provideGetCustomers(){
        return new GetCustomers(provideCustomersRepository());
    }

    private static CustomersRepository provideCustomersRepository() {
        return CustomersRepository.getInstance(CacheCustomersStore.getInstance());
    }

    public static GetInvoicesForUi provideGetInvoices() {
        return new GetInvoicesForUi(provideInvoicesRepository());
    }

    private static InvoicesRepository provideInvoicesRepository() {
        return InvoicesRepository.getInstance(
                CacheInvoicesStore.getInstance(provideCustomersRepository()));
    }

    public static SaveInvoice provideSaveInvoice(){
        return new SaveInvoice(provideInvoicesRepository());
    }

    public static CacheInvoiceItemsStore provideCacheInvoiceItemsStore(@NonNull Context context) {
        return CacheInvoiceItemsStore.getInstance(provideProductsRepository(context));
    }

}
