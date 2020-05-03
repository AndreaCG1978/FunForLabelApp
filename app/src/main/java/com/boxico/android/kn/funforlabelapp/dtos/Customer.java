package com.boxico.android.kn.funforlabelapp.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

public class Customer implements Serializable {

    @SerializedName("customers_id")
    @Expose
    private long id;

    @SerializedName("customers_firstname")
    @Expose
    private String firstName;

    @SerializedName("customers_lastname")
    @Expose
    private String lastName;

    @SerializedName("customers_email_address")
    @Expose
    private String email;

    @SerializedName("customers_password")
    @Expose
    private String password;

    @SerializedName("customers_gender")
    @Expose
    private String gender;

    @SerializedName("customers_ciudad")
    @Expose
    private String ciudad;

    @SerializedName("customers_provincia")
    @Expose
    private String provincia;

    @SerializedName("customers_suburbio")
    @Expose
    private String suburbio;

    @SerializedName("customers_direccion")
    @Expose
    private String direccion;

    @SerializedName("customers_cp")
    @Expose
    private String cp;

    @SerializedName("customers_telephone")
    @Expose
    private String telephone;

    @SerializedName("customers_fax")
    @Expose
    private String fax;

    @SerializedName("customers_newsletter")
    @Expose
    private String newsletter;


    @SerializedName("customers_dob")
    @Expose
    private Date dob;

    @SerializedName("customers_default_address_id")
    @Expose
    private long default_address_id;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public long getDefault_address_id() {
        return default_address_id;
    }

    public void setDefault_address_id(long default_address_id) {
        this.default_address_id = default_address_id;
    }

    private String notEncriptedPassword;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getSuburbio() {
        return suburbio;
    }

    public void setSuburbio(String suburbio) {
        this.suburbio = suburbio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(String newsletter) {
        this.newsletter = newsletter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotEncriptedPassword() {
        return notEncriptedPassword;
    }

    public void setNotEncriptedPassword(String notEncriptedPassword) {
        this.notEncriptedPassword = notEncriptedPassword;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
