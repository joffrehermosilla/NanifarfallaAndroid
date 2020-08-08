package nanifarfalla.app.invoices.data.cache;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nanifarfalla.app.customers.data.ICustomersRepository;
import nanifarfalla.app.customers.domain.criteria.CustomersByIds;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.invoices.domain.criteria.InvoicesUiSelector;
import nanifarfalla.app.invoices.domain.criteria.InvoicesSelector;
import nanifarfalla.app.invoices.domain.entities.CustomerUi;
import nanifarfalla.app.invoices.domain.entities.Invoice;
import nanifarfalla.app.invoices.domain.entities.InvoiceItem;
import nanifarfalla.app.invoices.domain.entities.InvoiceUi;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación de la cache para facturas
 */

public class CacheInvoicesStore implements ICacheInvoicesStore {

    private static CacheInvoicesStore INSTANCE = null;

    HashMap<String, Invoice> mCachedInvoices = null;

    private ICustomersRepository mCustomersRepo;


    private CacheInvoicesStore(ICustomersRepository customersRepository) {
        mCustomersRepo = Preconditions.checkNotNull(customersRepository,
                "customersRepository no puede ser null");

        mCachedInvoices = new LinkedHashMap<>();

        String[] customerIds = {
                "100001", "100002", "100003",
                "100004", "100005", "100006",
                "100007", "100008", "100009",
                "100010"};
        String[] invoiceStates = {
                Invoice.STATE_VALUE_CANCELED,
                Invoice.STATE_VALUE_DRAFT,
                Invoice.STATE_VALUE_PAID,
                Invoice.STATE_VALUE_SENT};
        String[] productIds = {
                "0002-4464", "0003-1968", "0006-0106", "0006-0705", "0006-4999",
                "0007-4140", "0009-0370", "0009-3169", "0013-0102", "0013-2586"};

        Random random = new Random();

        // Se generan 10 facturas de prueba
        for (int i = 0; i < 10; i++) {

            String invoiceId = UUID.randomUUID().toString();
            Invoice invoice = new Invoice(
                    invoiceId,
                    customerIds[random.nextInt(10)],
                    "INV-" + (i + 1),
                    DateTimeUtils.getTime(),
                    invoiceStates[random.nextInt(4)]);

            // Items
            for (int j = 0; j < random.nextInt(20) + 1; j++) {
                InvoiceItem invoiceItem = new InvoiceItem(
                        productIds[random.nextInt(10)],
                        random.nextInt(5) + 1,
                        random.nextFloat() * 10
                );

                invoice.addInvoiceItem(invoiceItem);
            }

            mCachedInvoices.put(invoice.getId(), invoice);
        }

    }

    public static CacheInvoicesStore getInstance(ICustomersRepository customersRepository) {
        if (INSTANCE == null) {
            INSTANCE = new CacheInvoicesStore(customersRepository);
        }

        return INSTANCE;
    }

    @Override
    public List<Invoice> getInvoices(Query query) {
        checkNotNull(query, "query no puede ser null");

        // Se obtienen facturas en forma de lista
        List<Invoice> invoices = Lists.newArrayList(mCachedInvoices.values());

        // Selección de facturas
        InvoicesSelector selector = new InvoicesSelector(query);
        return selector.selectListRows(invoices);
    }

    @Override
    public void getInvoicesUis(final Query query, final LoadInvoicesUiCallback callback) {

        List<String> customersIds = new ArrayList<>(0);

        for (Invoice invoice : mCachedInvoices.values()) {
            customersIds.add(invoice.getCustomerId());
        }

        // Clientes por conjunto de IDs
        Query customerQuery = new Query(new CustomersByIds(customersIds));

        mCustomersRepo.getCustomers(
                customerQuery,
                new ICustomersRepository.GetCustomersCallback() {
                    @Override
                    public void onCustomersLoaded(List<Customer> customers) {
                        List<InvoiceUi> invoiceUis = new ArrayList<>();

                        for (Customer customer : customers) {
                            Invoice invoice = findInvoiceByCustomerId(customer.getId());
                            InvoiceUi invoiceInfo = joinInvoiceCustomer(customer, invoice);

                            invoiceUis.add(invoiceInfo);
                        }

                        InvoicesUiSelector selector = new InvoicesUiSelector(query);

                        callback.onInvoicesUiLoaded(selector.selectListRows(invoiceUis));
                    }

                    @Override
                    public void onDataNotAvailable(String errorMsg) {
                        callback.onDataNotAvailable();
                    }
                });
    }

    private Invoice findInvoiceByCustomerId(final String customerId) {
        Collection<Invoice> invoices = mCachedInvoices.values();
        return Iterables.find(invoices, new Predicate<Invoice>() {
            @Override
            public boolean apply(Invoice invoice) {
                return invoice.getCustomerId().equals(customerId);
            }
        });
    }

    @Override
    public void addInvoice(Invoice invoice) {
        if (mCachedInvoices == null) {
            mCachedInvoices = new LinkedHashMap<>();
        }

        mCachedInvoices.put(invoice.getId(), invoice);
    }

    @Override
    public void deleteInvoices() {
        if (mCachedInvoices == null) {
            mCachedInvoices = new LinkedHashMap<>();
        }

        mCachedInvoices.clear();
    }

    @Override
    public boolean isCacheReady() {
        return mCachedInvoices != null;
    }

    @NonNull
    private InvoiceUi joinInvoiceCustomer(Customer customer, Invoice invoice) {
        CustomerUi customerUi = new CustomerUi(
                customer.getName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCity());

        return new InvoiceUi(
                invoice.getId(),
                invoice.getNumber(),
                customerUi,
                invoice.getDate(),
                invoice.getTotalAmount(),
                invoice.getState(),
                invoice.numberOfItems());
    }
}
