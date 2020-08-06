package nanifarfalla.app.addeditinvoice.domain.entities;

/**
 * Item de línea para UI
 */

public class InvoiceItemUi {
    private String mProductId;
    private int mQuantity;
    private float mPrice;
    private float mTotal;
    private int mItemNumber;
    private ProductUi mProductUi;

    public InvoiceItemUi(String productId,
                         int quantity,
                         float price,
                         float total,
                         int itemNumber,
                         ProductUi productUi) {
        mProductId = productId;
        mQuantity = quantity;
        mPrice = price;
        mTotal = total;
        mItemNumber = itemNumber;
        mProductUi = productUi;
    }

    private void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    private void setPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    private void setTotal(float mTotal) {
        this.mTotal = mTotal;
    }

    private void setProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public String getProductId() {
        return mProductId;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public float getItemPrice() {
        return mPrice;
    }

    public float getTotal() {
        return mTotal;
    }

    public String getProductName() {
        return mProductUi.getName();
    }

    public String getProductImageUrl() {
        return mProductUi.getImageUrl();
    }

    public int getProductStock() {
        return mProductUi.getStock();
    }

    public int getItemNumber() {
        return mItemNumber;
    }
}
