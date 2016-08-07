package com.pocketbank.lazylad91.pocketbank.Model;

import java.io.Serializable;

/**
 * Created by Parteek on 7/30/2016.
 */
public class PaymentMode implements Serializable {
    @Override
    public String toString() {
        return "PaymentMode{" +
                "cardType='" + cardType + '\'' +
                ", cardNumber=" + cardNumber +
                ", cardId=" + cardId +
                '}';
    }

    public PaymentMode() {

    }

    public PaymentMode(String cardType, int cardNumber, int cardId) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardId = cardId;
    }

    private String cardType;
    private int cardNumber;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    private int cardId;
}

