package nanifarfalla.app.addeditinvoice.presentation;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import nanifarfalla.app.addeditinvoiceitem.data.ICacheInvoiceItemsStore;
import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;
import nanifarfalla.app.customers.domain.criteria.CustomerById;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.customers.domain.usecases.IGetCustomers;
import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.usecases.ISaveInvoice;
import nanifarfalla.app.selection.Query;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Presentado de "Añadir Factura"
 */

public class AddEditInvoicePresenter implements AddEditInvoiceMvp.Presenter {

    // Relaciones
    private AddEditInvoiceMvp.View mView;
    private IGetCustomers mGetCustomer;
    private ISaveInvoice mSaveInvoice;
    private ICacheInvoiceItemsStore mCache;

    public AddEditInvoicePresenter(@NonNull AddEditInvoiceMvp.View view,
                                   @NonNull IGetCustomers getCustomer,
                                   @NonNull ISaveInvoice saveInvoice,
                                   @NonNull ICacheInvoiceItemsStore cache) {
        mView = Preconditions.checkNotNull(view);
        mGetCustomer = Preconditions.checkNotNull(getCustomer);
        mSaveInvoice = Preconditions.checkNotNull(saveInvoice);
        mCache = Preconditions.checkNotNull(cache);
    }

    @Override
    public void manageCustomerPickingResult(final String customerId) {
        loadCustomer(customerId);
    }

    private void loadCustomer(String customerId) {
        Query query = new Query(new CustomerById(customerId),
                null, 0, 0, 0);

        mGetCustomer.execute(query, false, new IGetCustomers.ExecuteCallback() {
            @Override
            public void onSuccess(List<Customer> customers) {
                if (!customers.isEmpty()) {
                    mView.showCustomer(customers.get(0).getName());
                }
            }

            @Override
            public void onError(String error) {
                mView.showCustomerError();
            }
        });
    }

    @Override
    public void manageAdditionResult() {

    }

    @Override
    public void manageEditionResult() {

    }

    @Override
    public void loadInvoiceItems() {
        loadInvoiceItemsUi();
    }

    @Override
    public void saveInvoice(String customerId, Date date) {

        Invoice newInvoice = new Invoice(
                customerId,
                date,
                mCache.getInvoiceItems(),
                mCache.getTotal());

        boolean invalidUserInput = false;

        if (newInvoice.emptyCustomer()) {
            mView.showCustomerError();
            invalidUserInput = true;
        }

        if (newInvoice.noItems()) {
            mView.showItemsError();
            invalidUserInput = true;
        }

        if (!invalidUserInput) {
            mSaveInvoice.execute(newInvoice, new ISaveInvoice.ExecuteCallback() {
                @Override
                public void onSuccess(Invoice invoice) {
                    mCache.deleteAll();
                    mView.showInvoicesScreen(invoice.getId());
                }

                @Override
                public void onError(String error) {
                    mView.showSaveError(error);
                }
            });
        }
    }

    @Override
    public void selectCustomer() {
        mView.showCustomersScreen();
    }

    @Override
    public void addNewInvoiceItem() {
        mView.showAddInvoiceItemScreen();
    }

    @Override
    public void editInvoiceItem(String productId) {
        mView.showEditInvoiceItemScreen(productId);
    }

    @Override
    public void deleteInvoiceItem(final String productId) {
        // Eliminar item
        mCache.deleteInvoiceItem(productId);

        // Cargar items restantes
        loadInvoiceItemsUi();
    }

    private void loadInvoiceItemsUi() {
        mCache.getInvoiceItemsUi(new ICacheInvoiceItemsStore.LoadInvoiceItemsUiCallback() {
            @Override
            public void onInvoiceItemsLoaded(List<InvoiceItemUi> invoiceItemUis) {

                // Actualizar lista de items
                mView.showInvoiceItems(invoiceItemUis);

                // Actualizar totales
                mView.showInvoiceAmounts(
                        formatAmount(mCache.getSubtotal()),
                        formatAmount(mCache.getTax()),
                        formatAmount(mCache.getTotal()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void restoreState(@NonNull String customerId) {
        loadCustomer(customerId);
    }

    private String formatAmount(float amount) {
        return String.format(Locale.ROOT, "$%.2f", amount);
    }
}
