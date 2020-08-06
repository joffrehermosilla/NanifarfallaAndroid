package nanifarfalla.app.invoices.domain.entities;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Objetos de dominio para las facturas
 */

public class Invoice {
    public static final String STATE_VALUE_DRAFT = "Borrador";
    public static final String STATE_VALUE_PAID = "Pagada";
    public static final String STATE_VALUE_CANCELED = "Cancelada";
    public static final String STATE_VALUE_SENT = "Enviada";

    private String mId;
    private String mCustomerId;
    private String mNumber;
    private Date mDate;
    private float mTotalAmount;
    private String mState;
    private List<InvoiceItem> mInvoiceItems;

    public Invoice(String id,
                   String customerId,
                   String number,
                   Date date,
                   float totalAmount,
                   String state,
                   List<InvoiceItem> invoiceItems) {
        mId = id;
        mCustomerId = customerId;
        mNumber = number;
        mInvoiceItems = invoiceItems;
        mDate = date;
        mTotalAmount = totalAmount;
        mState = state;
    }

    public Invoice(String customerId,
                   Date date,
                   List<InvoiceItem> items,
                   float totalAmount) {
        this(
                UUID.randomUUID().toString(),
                customerId,
                "INV-" + UUID.randomUUID().toString(),
                date,
                totalAmount,
                STATE_VALUE_DRAFT,
                items
        );
    }


    public Invoice(String id,
                   String customerId,
                   String number,
                   Date date,
                   String state) {

        this(
                id,
                customerId,
                number,
                date, 0, state,
                new ArrayList<InvoiceItem>(0)
        );
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public float getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.mTotalAmount = totalAmount;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public int numberOfItems() {
        return mInvoiceItems.size();
    }

    public String getCustomerId() {
        return mCustomerId;
    }

    public void addInvoiceItem(InvoiceItem item) {
        mTotalAmount += item.getTotal();
        mInvoiceItems.add(item);
    }

    public void removeInvoiceItem(InvoiceItem item) {
        mTotalAmount -= item.getTotal();
        mInvoiceItems.remove(item);
    }

    public boolean emptyCustomer() {
        return Strings.isNullOrEmpty(mCustomerId);
    }

    public boolean noItems() {
        return mInvoiceItems.isEmpty();
    }
}
