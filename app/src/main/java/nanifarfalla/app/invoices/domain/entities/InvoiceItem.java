package nanifarfalla.app.invoices.domain.entities;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

//import android.support.annotation.IntRange;

/**
 * Objetos de dominio para los ítems de facturas
 */

public class InvoiceItem {

    private String mProductId;
    private int mLineNumber;
    private int mQuantity;
    private float mPrice;
    private float mItemTotal;


    public InvoiceItem(@NonNull String productId,
                       @IntRange(from = 1) int lineNumber,
                       @IntRange(from = 1) int quantity,
                       @FloatRange(from = 0.0, fromInclusive = false) float price,
                       @FloatRange(from = 0.0, fromInclusive = false) float itemTotal) {
        mProductId = productId;
        mLineNumber = lineNumber;
        mQuantity = quantity;
        mPrice = price;
        mItemTotal = itemTotal;

    }

    public InvoiceItem(@NonNull String productId,
                       @IntRange(from = 1) int quantity,
                       @FloatRange(from = 0.0, fromInclusive = false) float price) {
        this(productId,
                1,
                quantity,
                price,
                quantity * price);
    }


    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public int getItemNumber() {
        return mLineNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.mLineNumber = itemNumber;
    }

    public void setId(String id) {
        mProductId = id;
    }

    public String getProductId() {
        return mProductId;
    }

    public float getTotal() {
        return mItemTotal;
    }

    public float getPrice() {
        return mPrice;
    }
}
