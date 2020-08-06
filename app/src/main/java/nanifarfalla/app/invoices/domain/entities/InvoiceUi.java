package nanifarfalla.app.invoices.domain.entities;

import java.util.Date;

/**
 * Facturas para UI lista
 */

public class InvoiceUi {
    private String mId;
    private String mNumber;
    private Date mDate;
    private float mTotalAmount;
    private String mState;
    private int mTotalItems;
    private CustomerUi mCustomerUi;

    public InvoiceUi(String id,
                     String number,
                     CustomerUi customerUi,
                     Date date,
                     float totalAmount,
                     String state,
                     int totalItems) {
        mId = id;
        mNumber = number;
        mCustomerUi = customerUi;
        mDate = date;
        mTotalAmount = totalAmount;
        mState = state;
        mTotalItems = totalItems;
    }

    private void setId(String id) {
        mId = id;
    }

    private void setNumber(String number) {
        mNumber = number;
    }

    private void setDate(Date date) {
        mDate = date;
    }

    private void setTotalAmount(float totalAmount) {
        mTotalAmount = totalAmount;
    }

    private void setState(String state) {
        mState = state;
    }

    public String getId() {
        return mId;
    }

    public String getNumber() {
        return mNumber;
    }

    public Date getDate() {
        return mDate;
    }

    public float getTotalAmount() {
        return mTotalAmount;
    }

    public String getState() {
        return mState;
    }

    public String getCustomerName() {
        return mCustomerUi.getName();
    }

    public int getTotalItems() {
        return mTotalItems;
    }
}
