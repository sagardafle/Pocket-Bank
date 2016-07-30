package com.pocketbank.lazylad91.pocketbank.Constant;

import com.pocketbank.lazylad91.pocketbank.Model.Category;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parteek on 7/29/2016.
 */
public class PocketBankConstant {
    public static ArrayList<Category> categoryList = new ArrayList<>();
    public static Category restaurant = new Category(0, "Restaurant", "categoryrestaurant");
    public static Category foodandbeverages = new Category(1, "Food and Beverages", "foodandbeverages");
    public static Category phoneBill = new Category(2, "Phone Bill", "categoryphone");
    public static Category internetbill = new Category(3, "Internet Bill", "categoryinternet");
    public static Category clothing = new Category(4, "Shopping", "clothing");
    public static Category friendsandlovers = new Category(5, "Friends and Lovers", "friendsandlovers");
    public static Category traveller = new Category(6, "Travel", "categorytravel");
    public static Category movies = new Category(7, "Movies", "movies");
    public static Category sports = new Category(8, "Fitness", "sports");
    public static Category childrensandbabies = new Category(9, "Children And Babies", "childrensandbabies");
    public static Category insurance = new Category(10, "Insurance", "categoryinsurance");
    public static Category car = new Category(11, "Car", "categorycar");
    public static Category gas = new Category(12, "Gas", "categorygas");
    public static Category education = new Category(13, "Education", "categoryeducation");
    public static Category grocery = new Category(14, "Grocery", "categorygrocery");
    public static Category drinks = new Category(15, "Drinks", "categorydrinks");
    public static Category medical = new Category(16, "Medical", "categorymedical");
    public static Category fitness = new Category(17, "Fitness", "categoryfitness");


    /*constant for month*/
    public static HashMap<Integer, String> monthMap = new HashMap<>();
}
