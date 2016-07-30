package com.pocketbank.lazylad91.pocketbank.Model;

import java.util.ArrayList;

/**
 * Created by Parteek on 7/30/2016.
 */
public class TransactionList {
    public TransactionList() {

    }

    public ArrayList<Transaction> getTransactionArrayList() {
        return mTransactionArrayList;
    }

    public void setTransactionArrayList(ArrayList<Transaction> transactionArrayList) {
        mTransactionArrayList = transactionArrayList;
    }

    private ArrayList<Transaction> mTransactionArrayList;
}
