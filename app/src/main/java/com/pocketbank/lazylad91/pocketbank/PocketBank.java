package com.pocketbank.lazylad91.pocketbank;

import android.app.Application;

import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;

/**
 * Created by Parteek on 7/29/2016.
 */
public class PocketBank extends Application {
    public PocketBank() {
        PocketBankConstant.categoryList.add(0, PocketBankConstant.grocery);
        PocketBankConstant.categoryList.add(1, PocketBankConstant.restaurant);
        PocketBankConstant.categoryList.add(2, PocketBankConstant.foodandbeverages);
        PocketBankConstant.categoryList.add(3, PocketBankConstant.car);
        PocketBankConstant.categoryList.add(4, PocketBankConstant.gas);
        PocketBankConstant.categoryList.add(5, PocketBankConstant.clothing);
        PocketBankConstant.categoryList.add(6, PocketBankConstant.drinks);
        PocketBankConstant.categoryList.add(7, PocketBankConstant.friendsandlovers);
        PocketBankConstant.categoryList.add(8, PocketBankConstant.traveller);
        PocketBankConstant.categoryList.add(9, PocketBankConstant.movies);
        PocketBankConstant.categoryList.add(10, PocketBankConstant.sports);
       // PocketBankConstant.categoryList.add(9, PocketBankConstant.childrensandbabies);
        PocketBankConstant.categoryList.add(11, PocketBankConstant.phoneBill);
        PocketBankConstant.categoryList.add(12, PocketBankConstant.internetbill);
        PocketBankConstant.categoryList.add(13, PocketBankConstant.education);
        PocketBankConstant.categoryList.add(14, PocketBankConstant.medical);
        PocketBankConstant.categoryList.add(15, PocketBankConstant.insurance);
       // PocketBankConstant.categoryList.add(17, PocketBankConstant.fitness);

    }
}
