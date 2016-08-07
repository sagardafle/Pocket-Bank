package com.pocketbank.lazylad91.pocketbank.Model;

import java.io.Serializable;

/**
 * Created by Parteek on 7/30/2016.
 */
public class Transaction implements Serializable {
    private String Uid;
    private String month;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    private int year;
    private int date;
    private float amount;

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    private String displayImage;
    private Category category;
    private PaymentMode mPaymentMode;
    private String notes;
    private Location Location;

    @Override
    public String toString() {
        return "Transaction{" +
                "Uid='" + Uid + '\'' +
                ", month='" + month + '\'' +
                ", year=" + year +
                ", amount=" + amount +
                ", category=" + category +
                ", mPaymentMode=" + mPaymentMode +
                ", notes='" + notes + '\'' +
                ", Location=" + Location +
                '}';
    }

    public Transaction(String uid, String month, int year, float amount, Category category, PaymentMode paymentMode, String notes, com.pocketbank.lazylad91.pocketbank.Model.Location location) {
        Uid = uid;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.category = category;
        mPaymentMode = paymentMode;
        this.notes = notes;
        Location = location;
    }

    public PaymentMode getPaymentMode() {
        return mPaymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        mPaymentMode = paymentMode;
    }

    public Transaction() {

    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }

    public com.pocketbank.lazylad91.pocketbank.Model.Location getLocation() {
        return Location;
    }

    public void setLocation(com.pocketbank.lazylad91.pocketbank.Model.Location location) {
        Location = location;
    }
}
