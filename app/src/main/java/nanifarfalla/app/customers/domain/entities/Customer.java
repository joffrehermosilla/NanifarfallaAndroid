package nanifarfalla.app.customers.domain.entities;

import java.util.UUID;

/**
 * Objeto de dominio para los clientes
 */

public class Customer {
    private String mId;
    private String mName;
    private String mPhone;
    private String mAddress;
    private String mCity;
    private String mRegisterDate;
    private String mOtherDetails;

    public Customer(String id, String name,
                    String phone, String address,
                    String city, String registerDate,
                    String otherDetails) {
        mId = id;
        mName = name;
        mPhone = phone;
        mAddress = address;
        mCity = city;
        mRegisterDate = registerDate;
        mOtherDetails = otherDetails;
    }

    public Customer(String name,
                    String phone, String address,
                    String city, String registerDate,
                    String otherDetails) {
        this(UUID.randomUUID().toString(),// ID random automático
                name,
                phone,
                address,
                city,
                registerDate,
                otherDetails);

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getRegisterDate() {
        return mRegisterDate;
    }

    public void setRegisterDate(String registerDate) {
        this.mRegisterDate = registerDate;
    }

    public String getOtherDetails() {
        return mOtherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.mOtherDetails = otherDetails;
    }
}
