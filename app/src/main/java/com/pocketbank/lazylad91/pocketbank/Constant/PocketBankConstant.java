package com.pocketbank.lazylad91.pocketbank.Constant;

import com.pocketbank.lazylad91.pocketbank.Model.Category;
import com.pocketbank.lazylad91.pocketbank.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Parteek on 7/29/2016.
 */
public class PocketBankConstant {
    public static ArrayList<Category> categoryList = new ArrayList<>();
    public static Category restaurant = new Category(1, "Restaurant", "categoryrestaurant");
    public static Category foodandbeverages = new Category(2, "Food and Beverages", "foodandbeverages");
    public static Category phoneBill = new Category(3, "Phone Bill", "categoryphone");
    public static Category internetbill = new Category(4, "Internet Bill", "categoryinternet");
    public static Category clothing = new Category(5, "Shopping", "clothing");
    public static Category friendsandlovers = new Category(6, "Friends and Lovers", "friendsandlovers");
    public static Category traveller = new Category(7, "Travel", "categorytravel");
    public static Category movies = new Category(8, "Movies", "movies");
    public static Category sports = new Category(9, "Fitness", "sports");
    public static Category childrensandbabies = new Category(10, "Children And Babies", "childrensandbabies");
    public static Category insurance = new Category(11, "Insurance", "categoryinsurance");
    public static Category car = new Category(12, "Car", "categorycar");
    public static Category gas = new Category(13, "Gas", "categorygas");
    public static Category education = new Category(14, "Education", "categoryeducation");
    public static Category grocery = new Category(15, "Grocery", "categorygrocery");
    public static Category drinks = new Category(16, "Drinks", "categorydrinks");
    public static Category medical = new Category(17, "Medical", "categorymedical");
    public static Category fitness = new Category(18, "Fitness", "categoryfitness");



    public static int getResId(String resName, Class<?> c) {

        try {
            Class res = R.drawable.class;
            Field field = res.getField("drawableName");
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
