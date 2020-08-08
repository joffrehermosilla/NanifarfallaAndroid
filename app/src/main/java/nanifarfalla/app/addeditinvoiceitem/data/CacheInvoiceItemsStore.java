package nanifarfalla.app.addeditinvoiceitem.data;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;
import nanifarfalla.app.invoices.domain.entities.InvoiceItem;
import nanifarfalla.app.addeditinvoice.domain.entities.ProductUi;
import nanifarfalla.app.products.data.IProductsRepository;
import nanifarfalla.app.products.domain.criteria.ProductsByCode;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.selection.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Caché para items de factura
 */

public class CacheInvoiceItemsStore implements ICacheInvoiceItemsStore {
    private static final float TAX_VALUE = 0.18f;

    private static CacheInvoiceItemsStore ourInstance = null;

    private Map<String, InvoiceItem> mCachedInvoiceItems = new LinkedHashMap<>();

    private static IProductsRepository mProductsRepository;

    // Totales
    private float mTotal;
    private float mTax;
    private float mSubtotal;

    public static CacheInvoiceItemsStore getInstance(IProductsRepository productsRepository) {
        if (ourInstance == null) {
            ourInstance = new CacheInvoiceItemsStore(productsRepository);
        }
        return ourInstance;
    }

    private CacheInvoiceItemsStore(IProductsRepository productsRepository) {
        mProductsRepository = Preconditions.checkNotNull(productsRepository);
        mTotal = mSubtotal = mTax = 0;
    }

    @Override
    public void saveInvoiceItem(@NonNull InvoiceItem invoiceItem) {
        InvoiceItem oldInvoiceItem = mCachedInvoiceItems.put(invoiceItem.getProductId(), invoiceItem);

        if (oldInvoiceItem != null) { // Edición
            float diff = oldInvoiceItem.getTotal() - invoiceItem.getTotal();
            calculateAmounts(-diff);
        } else {// Adición
            calculateAmounts(invoiceItem.getTotal());
            generateItemNumbers();
        }
    }

    @Override
    public float getTotal() {
        return mTotal;
    }

    @Override
    public float getSubtotal() {
        return mSubtotal;
    }

    @Override
    public float getTax() {
        return mTax;
    }

    @Override
    public List<InvoiceItem> getInvoiceItems() {
        return getValues();
    }

    @Override
    public void getInvoiceItemsUi(@NonNull final LoadInvoiceItemsUiCallback callback) {

        final List<InvoiceItemUi> invoiceItemsUi = new ArrayList<>(0);

        ArrayList<String> codes = getKeys();
        Query query = new Query(new ProductsByCode(codes));

        mProductsRepository.getProducts(query, new IProductsRepository.GetProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                for (Product product : products) {
                    InvoiceItem invoiceItem = findInvoiceItem(product.getCode());

                    invoiceItemsUi.add(joinInvoiceItemAndProduct(invoiceItem, product));
                }

                sortByNumberItem(invoiceItemsUi);

                callback.onInvoiceItemsLoaded(invoiceItemsUi);
            }

            @Override
            public void onDataNotAvailable(String error) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getInvoiceItemUi(@NonNull final String productId, @NonNull final GetInvoiceItemUiCallback callback) {
        List<String> codes = new ArrayList<>();
        codes.add(productId);

        Query query = new Query(new ProductsByCode(codes));

        mProductsRepository.getProducts(query, new IProductsRepository.GetProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> products) {
                if (products.isEmpty()) {
                    callback.onDataNotAvailable();
                } else {
                    Product product = products.get(0);
                    InvoiceItem itemInvoice = findInvoiceItem(product.getCode());
                    callback.onInvoiceItemUiLoaded(joinInvoiceItemAndProduct(itemInvoice, product));
                }
            }


            @Override
            public void onDataNotAvailable(String error) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteInvoiceItem(@NonNull String productId) {
        InvoiceItem removedItem = mCachedInvoiceItems.remove(productId);
        calculateAmounts(-removedItem.getTotal());
        generateItemNumbers();
    }

    @Override
    public void deleteAll() {
        mCachedInvoiceItems.clear();
        mTotal = mSubtotal = mTax = 0;
    }

    @NonNull
    private ArrayList<InvoiceItem> getValues() {
        return Lists.newArrayList(mCachedInvoiceItems.values());
    }

    @NonNull
    private ArrayList<String> getKeys() {
        return Lists.newArrayList(mCachedInvoiceItems.keySet());
    }

    private void generateItemNumbers() {
        ArrayList<InvoiceItem> values = getValues();
        for (int i = mCachedInvoiceItems.size() - 1; i > 0; i--) {
            values.get(i).setItemNumber(i + 1);
        }
    }

    private void calculateAmounts(float itemTotal) {
        mTotal += itemTotal;
        mTax = mTotal * TAX_VALUE;
        mSubtotal = mTotal - mTax;
    }

    private void sortByNumberItem(List<InvoiceItemUi> invoiceItemsUi) {
        Collections.sort(invoiceItemsUi, new Comparator<InvoiceItemUi>() {
            @Override
            public int compare(InvoiceItemUi o1, InvoiceItemUi o2) {
                return o1.getItemNumber() - o2.getItemNumber();
            }
        });
    }

    private InvoiceItem findInvoiceItem(final String code) {
        List<InvoiceItem> invoiceItems = getValues();
        return Iterables.find(invoiceItems, new Predicate<InvoiceItem>() {
            @Override
            public boolean apply(InvoiceItem invoiceItem) {
                return code.equals(invoiceItem.getProductId());
            }
        });
    }

    private InvoiceItemUi joinInvoiceItemAndProduct(InvoiceItem invoiceItem, Product product) {
        ProductUi productUi = new ProductUi(
                product.getName(),
                product.getUnitsInStock(),
                product.getImageUrl());
        InvoiceItemUi itemUi = new InvoiceItemUi(
                invoiceItem.getProductId(),
                invoiceItem.getQuantity(),
                invoiceItem.getPrice(),
                invoiceItem.getTotal(),
                invoiceItem.getItemNumber(),
                productUi);
        return itemUi;
    }

}
