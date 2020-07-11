package com.boxico.android.kn.funforlabelapp.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressBook {

    @SerializedName("customers_id")
    @Expose
    private long customerId;

    @SerializedName("address_book_id")
    @Expose
    private long addressId;

    @SerializedName("entry_street_address")
    @Expose
    private String calle;

    @SerializedName("entry_suburb")
    @Expose
    private String suburbio;

    @SerializedName("entry_postcode")
    @Expose
    private String cp;


    @SerializedName("entry_city")
    @Expose
    private String ciudad;

    @SerializedName("entry_state")
    @Expose
    private String provincia;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getSuburbio() {
        return suburbio;
    }

    public void setSuburbio(String suburbio) {
        this.suburbio = suburbio;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
