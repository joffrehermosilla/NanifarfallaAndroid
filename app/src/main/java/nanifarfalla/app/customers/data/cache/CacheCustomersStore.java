package nanifarfalla.app.customers.data.cache;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import nanifarfalla.app.customers.domain.criteria.CustomersSelector;
import nanifarfalla.app.customers.domain.entities.Customer;
import nanifarfalla.app.selection.Query;
import nanifarfalla.app.util.DateTimeUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementación de la caché para clientes
 */

public class CacheCustomersStore implements ICacheCustomersStore {
    /**
     * Única instancia de la clase
     */
    private static CacheCustomersStore INSTANCE = null;

    /**
     * Este campo representa la caché fundamental de clientes
     */
    HashMap<String, Customer> mCachedCustomers = null;

    // Se previene la creación de instancias
    private CacheCustomersStore() {
        mCachedCustomers = new LinkedHashMap<>();
        String currentDateTimeString = DateTimeUtils.getDateTime(DateTimeUtils.DATE_TIME_PATTERN);

        createTestCustomer(new Customer("100001", "Carlos Villamaria", "444 6661",
                "Cra 56", "Armenia", currentDateTimeString, null));
        createTestCustomer(new Customer("100002", "Miriam Stevia", "444 6662",
                "Cra 45", "Cali", currentDateTimeString, null));
        createTestCustomer(new Customer("100003", "Pedro Cigueña", "444 6663",
                "Cra 46", "Manizales", currentDateTimeString, null));
        createTestCustomer(new Customer("100004", "Sara Vives", "444 6664",
                "Cra 57", "Cali", currentDateTimeString, null));
        createTestCustomer(new Customer("100005", "Jose Lopez", "444 6665",
                "Cra 58", "Armenia", currentDateTimeString, null));
        createTestCustomer(new Customer("100006", "Claudia Suarez", "444 6666",
                "Cra 59", "Armenia", currentDateTimeString, null));
        createTestCustomer(new Customer("100007", "Esteban Soto", "444 6667",
                "Cra 60", "Cali", currentDateTimeString, null));
        createTestCustomer(new Customer("100008", "Samanta Lleras", "444 6668",
                "Cra 51", "Barranquilla", currentDateTimeString, null));
        createTestCustomer(new Customer("100009", "Jean Estupiñan", "444 6669",
                "Cra 52", "Pereira", currentDateTimeString, null));
        createTestCustomer(new Customer("100010", "Marcela Esturias", "444 6670",
                "Cra 53", "Medellín", currentDateTimeString, null));
    }

    /**
     * Añade un nuevo cliente a la caché
     */
    private void createTestCustomer(@NonNull Customer customer) {
        Preconditions.checkNotNull(customer, "customer no puede ser null");
        mCachedCustomers.put(customer.getId(), customer);
    }

    /**
     * Se crea o retorna la única instancia
     */
    public static CacheCustomersStore getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheCustomersStore();
        }
        return INSTANCE;
    }

    @Override
    public List<Customer> getCustomers(Query query) {
        checkNotNull(query, "query no puede ser null");

        // Se obtienen clientes en forma de lista
        List<Customer> customers = Lists.newArrayList(mCachedCustomers.values());

        // Selección de clientes
        CustomersSelector selector = new CustomersSelector(query);
        return selector.selectListRows(customers);
    }

    @Override
    public void addCustomer(Customer customer) {
        if (mCachedCustomers == null) {
            mCachedCustomers = new LinkedHashMap<>();
        }
        mCachedCustomers.put(customer.getId(), customer);
    }

    @Override
    public void deleteCustomers() {
        if (mCachedCustomers == null) {
            mCachedCustomers = new LinkedHashMap<>();
        }
        mCachedCustomers.clear();
    }

    @Override
    public boolean isCacheReady() {
        return mCachedCustomers != null;
    }
}
