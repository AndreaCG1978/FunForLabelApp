package com.boxico.android.kn.funforlabelapp.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

    private String notEncriptedPassword;


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
