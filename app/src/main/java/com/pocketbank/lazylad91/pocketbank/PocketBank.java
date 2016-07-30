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
        PocketBankConstant.categoryList.add(10, PocketBankConstant.insurance);
        PocketBankConstant.categoryList.add(11, PocketBankConstant.car);
        PocketBankConstant.categoryList.add(12, PocketBankConstant.gas);
        PocketBankConstant.categoryList.add(13, PocketBankConstant.education);
        PocketBankConstant.categoryList.add(14, PocketBankConstant.grocery);
        PocketBankConstant.categoryList.add(15, PocketBankConstant.drinks);
        PocketBankConstant.categoryList.add(16, PocketBankConstant.medical);
        PocketBankConstant.categoryList.add(17, PocketBankConstant.fitness);
        PocketBankConstant.monthMap.put(1, "January");
        PocketBankConstant.monthMap.put(2, "February");
        PocketBankConstant.monthMap.put(3, "March");
        PocketBankConstant.monthMap.put(4, "April");
        PocketBankConstant.monthMap.put(5, "May");
        PocketBankConstant.monthMap.put(6, "June");
        PocketBankConstant.monthMap.put(7, "July");
        PocketBankConstant.monthMap.put(8, "August");
        PocketBankConstant.monthMap.put(9, "September");
        PocketBankConstant.monthMap.put(10, "October");
        PocketBankConstant.monthMap.put(11, "November");
        PocketBankConstant.monthMap.put(12, "December");
    }
}
