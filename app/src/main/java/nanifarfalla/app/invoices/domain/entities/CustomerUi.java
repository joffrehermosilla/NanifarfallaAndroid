package nanifarfalla.app.invoices.domain.entities;

/**
 * Cliente para UI de factura
 */

public class CustomerUi {
    private String mName;
    private String mPhone;
    private String mAddress;
    private String mCity;

    public CustomerUi(String name,
                      String phone,
                      String address,
                      String city) {
        mName = name;
        mPhone = phone;
        mAddress = address;
        mCity = city;
    }

    private void setName(String name) {
        this.mName = name;
    }

    private void setPhone(String phone) {
        this.mPhone = phone;
    }

    private void setAddress(String address) {
        this.mAddress = address;
    }

    private void setCity(String city) {
        this.mCity = city;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCity() {
        return mCity;
    }
}
