package com.pocketbank.lazylad91.pocketbank;

import android.app.Application;

import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;

/**
 * Created by Parteek on 7/29/2016.
 */
public class PocketBank extends Application {
    public PocketBank() {
        PocketBankConstant.categoryList.add(0, PocketBankConstant.restaurant);
        PocketBankConstant.categoryList.add(1, PocketBankConstant.foodandbeverages);
        PocketBankConstant.categoryList.add(2, PocketBankConstant.phoneBill);
        PocketBankConstant.categoryList.add(3, PocketBankConstant.internetbill);
        PocketBankConstant.categoryList.add(4, PocketBankConstant.clothing);
        PocketBankConstant.categoryList.add(5, PocketBankConstant.friendsandlovers);
        PocketBankConstant.categoryList.add(6, PocketBankConstant.traveller);
        PocketBankConstant.categoryList.add(7, PocketBankConstant.movies);
        PocketBankConstant.categoryList.add(8, PocketBankConstant.sports);
        PocketBankConstant.categoryList.add(9, PocketBankConstant.childrensandbabies);
    }
}
