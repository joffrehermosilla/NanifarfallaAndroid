package nanifarfalla.app.addeditinvoiceitem.presentation;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import nanifarfalla.app.R;
import nanifarfalla.app.addeditinvoiceitem.data.ICacheInvoiceItemsStore;
import nanifarfalla.app.addeditinvoice.domain.entities.InvoiceItemUi;
import nanifarfalla.app.invoices.domain.entities.InvoiceItem;
import nanifarfalla.app.productdetail.domain.criteria.ProductByCode;
import nanifarfalla.app.products.domain.model.Product;
import nanifarfalla.app.products.domain.usecases.IGetProducts;
import nanifarfalla.app.selection.Query;

import java.util.List;

/**
 * Presentador de "Añadir/Editar items de factura"
 */

public class AddEditInvoiceItemPresenter implements AddEditInvoiceItemMvp.Presenter {

    private final AddEditInvoiceItemMvp.View mView;
    private final IGetProducts mGetProductByCode;
    private final ICacheInvoiceItemsStore mInvoiceItemsCache;
    private final Resources mResources;

    private String mProductId;

    private float mItemPrice;
    private int mStock;

    public AddEditInvoiceItemPresenter(@Nullable String productId,
                                       @NonNull AddEditInvoiceItemMvp.View view,
                                       @NonNull IGetProducts getProductByCode,
                                       @NonNull ICacheInvoiceItemsStore invoiceItemsCache,
                                       @NonNull Resources resources) {
        mProductId = productId;
        mView = Preconditions.checkNotNull(view);
        mGetProductByCode = Preconditions.checkNotNull(getProductByCode);
        mInvoiceItemsCache = Preconditions.checkNotNull(invoiceItemsCache);
        mResources = Preconditions.checkNotNull(resources);
    }

    @Override
    public void populateInvoiceItem() {
        if (isEdition()) {
            mInvoiceItemsCache.getInvoiceItemUi(mProductId,
                    new ICacheInvoiceItemsStore.GetInvoiceItemUiCallback() {
                        @Override
                        public void onInvoiceItemUiLoaded(InvoiceItemUi invoiceItemUi) {

                            mItemPrice = invoiceItemUi.getItemPrice();
                            mStock = invoiceItemUi.getProductStock();

                            showItem(invoiceItemUi.getProductName(),
                                    mStock,
                                    invoiceItemUi.getQuantity());
                        }

                        @Override
                        public void onDataNotAvailable() {
                            mView.showMissingInvoiceItem();
                        }
                    });

        }
    }

    @Override
    public void saveInvoiceItem(String selectedProductId, String productName,
                                String quantityString) {
        String productId;
        int quantity = 1;

        if (!quantityString.isEmpty()) {
            quantity = Integer.parseInt(quantityString);
        }

        if (Strings.isNullOrEmpty(productName)) {
            mView.showProductNotSelectedError();
            return;
        }

        if (quantity > mStock) {
            mView.showQuantityError();
            return;
        }

        productId = selectedProductId == null ? mProductId : selectedProductId;

        InvoiceItem invoiceItem = new InvoiceItem(
                productId,
                quantity,
                mItemPrice);

        mInvoiceItemsCache.saveInvoiceItem(invoiceItem);
        mView.showAddEditInvoiceScreen();

    }

    @Override
    public void restoreState(String productId) {
        manageProductPickingResult(productId);
    }

    @Override
    public void selectProduct() {
        mView.showProductsScreen();
    }

    @Override
    public void manageProductPickingResult(final String productId) {
        if (Strings.isNullOrEmpty(productId)) {
            mView.showMissingProduct();
            return;
        }

        Query query = new Query(new ProductByCode(productId));
        mGetProductByCode.getProducts(query, false, new IGetProducts.GetProductsCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                Product selectedProduct = products.get(0);

                mItemPrice = selectedProduct.getPrice();
                mStock = selectedProduct.getUnitsInStock();

                showItem(selectedProduct.getName(), mStock, 1);
            }

            @Override
            public void onError(String error) {
                mView.showMissingProduct();
            }
        });
    }

    private void showItem(String productName, int productStock, int quantity) {
        String stock = mResources.getString(R.string.product_stock,
                productStock);
        String quantityString = String.valueOf(quantity);

        mView.showProductName(productName);
        mView.showStock(stock);
        mView.showQuantity(quantityString);
    }

    private boolean isEdition() {
        return mProductId != null;
    }
}
