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
    public static Category restaurant = new Category(1, "Restaurant", "restaurant");
    public static Category foodandbeverages = new Category(2, "Food and Beverages", "foodandbeverages");
    public static Category phoneBill = new Category(3, "Phone Bill", "phonebill");
    public static Category internetbill = new Category(4, "Internet Bill", "internetbill");
    public static Category clothing = new Category(5, "Shopping", "clothing");
    public static Category friendsandlovers = new Category(6, "Friends and Lovers", "friendsandlovers");
    public static Category traveller = new Category(7, "Travel", "traveller");
    public static Category movies = new Category(8, "Movies", "movies");
    public static Category sports = new Category(9, "Fitness", "sports");
    public static Category childrensandbabies = new Category(10, "Children And Babies", "childrensandbabies");


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
