package com.pocketbank.lazylad91.pocketbank;

/*import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;*/

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deange.ropeprogressview.RopeProgressBar;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RopeProgressBar salaryProgress;
    private TextView salaryTextView;
    private ArcProgress restaurantProgress;
    private ArcProgress shoppingProgress;
    private ArcProgress travelProgress;
    private ArcProgress savingProgress;
    private static Double budgetSaving;
    private static Double totalSalary;
    private static Double budgetShopping;
    private static Double spendShopping;
    private static Double budgetTravel;
    private static Double spendTravel;
    private static Double budgetRestaurant;
    private static Double spendRestaurant;
    private static Double totalMonthExpenses;
    private static String uid;
    private static String month;
    private static String year;
    private static int monthNo;
    private static SharedPreferences sharedPref;
    private static DatabaseReference mDatabase;
    private static DatabaseReference bDatabase;
    private static ArrayList<Transaction> restaurantList;
    private static ArrayList<Transaction> shoppingList;
    private static ArrayList<Transaction> travelList;
    private static ArrayList<Transaction> totalList;
    private static HashMap<String,String> resultMap;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        bindActivity(view);

        mAppBarLayout.addOnOffsetChangedListener(this);
        // Creating shared preferences code start

        sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fcdn.shopify.com%2Fs%2Ffiles%2F1%2F0972%2F6232%2Ffiles%2Fno-image-placeholder.png&imgrefurl=http%3A%2F%2Fwww.case-mate.com%2Fproducts%2Fclear-pink-iphone-6-6s-tough-air-case&docid=5WemYxtoTFgVNM&tbnid=DAe83sw0MWdMiM%3A&w=1200&h=1200&bih=1012&biw=2133&ved=0ahUKEwi-mOPz1JzOAhVJ72MKHdcIADUQMwgfKAMwAw&iact=mrc&uact=8";
        String url = sharedPref.getString("url", defaultValue);
        defaultValue="Unknown";
        String name = sharedPref.getString("name",defaultValue);
        String email = sharedPref.getString("email",defaultValue);
        // creating shared preferences code stop
        // mToolbar.inflateMenu(view.R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        //You can write your code
        salaryTextView = (TextView) view.findViewById(R.id.salaryTextView);
        ImageView userImage = (ImageView) view.findViewById(R.id.userImage);
        TextView userNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        userNameTextView.setText(name);
        TextView emaiTextView = (TextView) view.findViewById(R.id.emailTextView);
        emaiTextView.setText(email);



        userImage.setImageResource(R.drawable.gg);
        CircleImageView imgview = (CircleImageView) view.findViewById(R.id.userImageIcon);
        Picasso.with(getActivity())
                .load(url)
                .fit()
                .into(imgview);
        salaryTextView = (TextView) view.findViewById(R.id.salaryTextView);
        salaryProgress = (RopeProgressBar) view.findViewById(R.id.remainingSalary);
         travelProgress = (ArcProgress) view.findViewById(R.id.arc_progress);
        shoppingProgress = (ArcProgress) view.findViewById(R.id.arc_progress1);
        restaurantProgress = (ArcProgress) view.findViewById(R.id.arc_progress2);
        savingProgress = (ArcProgress) view.findViewById(R.id.arc_progress3);
         restaurantList = new ArrayList<Transaction>();
         shoppingList = new ArrayList<Transaction>();
         travelList = new ArrayList<Transaction>();
         totalList = new ArrayList<Transaction>();
        resultMap = new HashMap<>();
        callDb();
        return view;
    }

    private void callDb() {
         /* Get current Month and year */
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if(year == null)
            year=String.valueOf(calendar.get(Calendar.YEAR));
        if(month == null) {
            month = PocketBankConstant.monthMap.get(calendar.get(Calendar.MONTH));
            monthNo = calendar.get(Calendar.MONTH);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        bDatabase = FirebaseDatabase.getInstance().getReference();
        String defaultValue = "null";
        uid = sharedPref.getString("uid", defaultValue);

        mDatabase.child(uid).child(year).child(month).child("transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               /* transactionHashMap =  (HashMap<String,Transaction>)dataSnapshot.getValue();*/
                //int transaction = Log.d("transaction", transactionHashMap.toString());
                totalList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Log.d("keyfire3", messageSnapshot.getChildren().toString());
                    Transaction transaction = new Transaction();
                    Log.d("keyfire3===========", messageSnapshot.child("amount").getValue().toString());

                    if(messageSnapshot.child("amount").exists()) {
                        transaction.setAmount(Float.parseFloat(messageSnapshot.child("amount").getValue().toString()));
                    }
                    if(messageSnapshot.child("month").exists()) {
                        transaction.setMonth(messageSnapshot.child("month").getValue().toString());
                    }
                    if(messageSnapshot.child("notes").exists()) {
                        transaction.setNotes(messageSnapshot.child("notes").getValue().toString());
                    }
                    if(messageSnapshot.child("date").exists()) {
                        transaction.setDate(Integer.parseInt(messageSnapshot.child("date").getValue().toString()));
                    }
                    if(messageSnapshot.child("uid").exists()) {
                        transaction.setUid(messageSnapshot.child("uid").getValue().toString());
                    }

                    if(messageSnapshot.child("year").exists()) {
                        transaction.setYear(Integer.parseInt(messageSnapshot.child("year").getValue().toString()));
                    }

                    if(messageSnapshot.child("category").child("id").exists()) {
                        transaction.setCategory(PocketBankConstant.categoryList.get(Integer.parseInt(messageSnapshot.child("category").child("id").getValue().toString())));
                    }

                    Location location = new Location();
                    if(messageSnapshot.child("location").child("lng").exists()) {
                        location.setLng(Double.parseDouble(messageSnapshot.child("location").child("lng").getValue().toString()));
                    }

                    if(messageSnapshot.child("location").child("lat").exists()) {
                        location.setLat(Double.parseDouble(messageSnapshot.child("location").child("lat").getValue().toString()));
                    }

                    if(messageSnapshot.child("location").child("name").exists()) {
                        location.setName(messageSnapshot.child("location").child("name").getValue().toString());
                    }

                    transaction.setLocation(location);
                    totalList.add(transaction);
                }
                UpdateCategoryList();
                bDatabase.child(uid).child("Budget").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null){
                            resultMap.put(dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
                        Log.d("resultMap", resultMap.toString());
                        if (resultMap.containsKey("4")) {
                            budgetShopping = Double.parseDouble(resultMap.get("4"));
                        }
                        if (resultMap.containsKey("0")) {
                            budgetRestaurant = Double.parseDouble(resultMap.get("0"));
                        }
                        if (resultMap.containsKey("6")) {
                            budgetTravel = Double.parseDouble(resultMap.get("6"));
                        }
                        if (resultMap.containsKey("Salary")) {
                            totalSalary = Double.parseDouble(resultMap.get("Salary"));
                        }
                        if (resultMap.containsKey("Savings")) {
                            budgetSaving = Double.parseDouble(resultMap.get("Savings"));
                        }
                        if (resultMap.containsKey("0") && resultMap.containsKey("4") && resultMap.containsKey("6") && resultMap.containsKey("Salary") && resultMap.containsKey("Savings")) {
                            restaurantProgress.setMax(100);
                            shoppingProgress.setMax(100);
                            travelProgress.setMax(100);
                            savingProgress.setMax(100);
                            Double restaurantPercentage=((spendRestaurant/ budgetRestaurant)*100);
                            Double shoppingPercentage= (spendShopping/budgetShopping)*100;
                            Double travelPercentage=(spendTravel/budgetTravel)*100;
                            Log.d("values12",spendTravel.toString());
                            Log.d("values12",budgetTravel.toString());
                            restaurantProgress.setProgress(restaurantPercentage.intValue());
                            shoppingProgress.setProgress(shoppingPercentage.intValue());
                            travelProgress.setProgress(travelPercentage.intValue());
                            Log.d("values1",totalMonthExpenses.toString());
                            Log.d("values1",budgetSaving.toString());
                            Log.d("values1",totalSalary.toString());
                            salaryTextView.setText(totalMonthExpenses+"/"+totalSalary);
                           // salaryTextView.setTextColor(Color.GREEN);
                            if(totalSalary-totalMonthExpenses>=budgetSaving) {
                                savingProgress.setProgress(100);
                            }
                            else{
                                Double savingPercentage = ((totalSalary-totalMonthExpenses)/budgetSaving)*100;
                                savingProgress.setProgress(savingPercentage.intValue());
                                savingProgress.setFinishedStrokeColor(Color.RED);
                            }
                            travelProgress.setBottomTextSize(45);
                            travelProgress.setStrokeWidth(17);
                            restaurantProgress.setBottomTextSize(45);
                            restaurantProgress.setStrokeWidth(17);
                            savingProgress.setBottomTextSize(45);
                            savingProgress.setStrokeWidth(17);
                            shoppingProgress.setBottomTextSize(45);
                            shoppingProgress.setStrokeWidth(17);
                            if(restaurantPercentage.intValue()>80){
                                restaurantProgress.setFinishedStrokeColor(Color.RED);
                            }
                            if(shoppingPercentage.intValue()>80){
                                shoppingProgress.setFinishedStrokeColor(Color.RED);
                            }
                            if(travelPercentage.intValue()>80){
                                Log.d("travelPercentage",travelPercentage.toString());
                                travelProgress.setFinishedStrokeColor(Color.RED);
                            }
                            Double salaryPercentage = ((totalMonthExpenses)/totalSalary)*100;
                            salaryProgress.setProgress(salaryPercentage.intValue());
                            if(salaryPercentage>80){
                                salaryProgress.setPrimaryColor(Color.RED);
                            }
                        }
                    }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null){
                            resultMap.put(dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
                            Log.d("resultMap", resultMap.toString());
                            if (resultMap.containsKey("4")) {
                                budgetShopping = Double.parseDouble(resultMap.get("4"));
                            }
                            if (resultMap.containsKey("0")) {
                                budgetRestaurant = Double.parseDouble(resultMap.get("0"));
                            }
                            if (resultMap.containsKey("6")) {
                                budgetTravel = Double.parseDouble(resultMap.get("6"));
                            }
                            if (resultMap.containsKey("Salary")) {
                                totalSalary = Double.parseDouble(resultMap.get("Salary"));
                            }
                            if (resultMap.containsKey("Savings")) {
                                budgetSaving = Double.parseDouble(resultMap.get("Savings"));
                            }
                            if (resultMap.containsKey("0") && resultMap.containsKey("4") && resultMap.containsKey("6") && resultMap.containsKey("Salary") && resultMap.containsKey("Savings")) {
                                restaurantProgress.setMax(100);
                                shoppingProgress.setMax(100);
                                travelProgress.setMax(100);
                                savingProgress.setMax(100);
                                Double restaurantPercentage=((spendRestaurant/ budgetRestaurant)*100);
                                Double shoppingPercentage= (spendShopping/budgetShopping)*100;
                                Double travelPercentage=(spendTravel/budgetTravel)*100;
                                Log.d("values12",spendTravel.toString());
                                Log.d("values12",budgetTravel.toString());
                                restaurantProgress.setProgress(restaurantPercentage.intValue());
                                shoppingProgress.setProgress(shoppingPercentage.intValue());
                                travelProgress.setProgress(travelPercentage.intValue());
                                Log.d("values1",totalMonthExpenses.toString());
                                Log.d("values1",budgetSaving.toString());
                                Log.d("values1",totalSalary.toString());
                                salaryTextView.setText("$ "+totalMonthExpenses+"/"+totalSalary);
                                if(totalSalary-totalMonthExpenses>=budgetSaving) {
                                    savingProgress.setProgress(100);
                                }
                                else{
                                    Double savingPercentage = ((totalSalary-totalMonthExpenses)/budgetSaving)*100;
                                    savingProgress.setProgress(savingPercentage.intValue());
                                    savingProgress.setFinishedStrokeColor(Color.RED);
                                }
                                travelProgress.setBottomTextSize(45);
                                travelProgress.setStrokeWidth(17);
                                restaurantProgress.setBottomTextSize(45);
                                restaurantProgress.setStrokeWidth(17);
                                savingProgress.setBottomTextSize(45);
                                savingProgress.setStrokeWidth(17);
                                shoppingProgress.setBottomTextSize(45);
                                shoppingProgress.setStrokeWidth(17);
                                if(restaurantPercentage.intValue()>80){
                                    restaurantProgress.setFinishedStrokeColor(Color.RED);
                                }
                                if(shoppingPercentage.intValue()>80){
                                    shoppingProgress.setFinishedStrokeColor(Color.RED);
                                }
                                if(travelPercentage.intValue()>80){
                                    Log.d("travelPercentage",travelPercentage.toString());
                                    travelProgress.setFinishedStrokeColor(Color.RED);
                                }
                                Double salaryPercentage = ((totalMonthExpenses)/totalSalary)*100;
                                salaryProgress.setProgress(salaryPercentage.intValue());
                                if(salaryPercentage>80){
                                    salaryProgress.setPrimaryColor(Color.RED);
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        //Log.d("initializeData",s);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("initializeData",s+""+"onChildAdded");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("keyFiredCancel", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        restaurantProgress.setBottomText("Restaurant");
        shoppingProgress.setBottomText("Shopping");
        travelProgress.setBottomText("Travel");
        savingProgress.setBottomText("Saving");
        savingProgress.setTextColor(Color.WHITE);
        restaurantProgress.setTextColor(Color.WHITE);
        shoppingProgress.setTextColor(Color.WHITE);
        travelProgress.setTextColor(Color.WHITE);

    }

    private void UpdateCategoryList() {
        restaurantList.clear();
        shoppingList.clear();
        travelList.clear();
        for(Transaction txn : totalList){
            if(txn.getCategory().getName().equals(PocketBankConstant.categoryList.get(0).getName())){
                restaurantList.add(txn);
            }
            else if(txn.getCategory().getName().equals(PocketBankConstant.categoryList.get(4).getName())){
                shoppingList.add(txn);
            }
            else if(txn.getCategory().getName().equals(PocketBankConstant.categoryList.get(6).getName())){
                travelList.add(txn);
            }
        }
        calculateSumTotal();
    }

    private void calculateSumTotal() {
        spendShopping=0.0;
        spendRestaurant=0.0;
        spendTravel=0.0;
        totalMonthExpenses=0.0;
        for(Transaction transaction : shoppingList){
            spendShopping=spendShopping+transaction.getAmount();
        }
        for(Transaction transaction : restaurantList){
            spendRestaurant=spendRestaurant+transaction.getAmount();
        }
        for(Transaction transaction : travelList){
            spendTravel=spendTravel+transaction.getAmount();
        }
        for(Transaction transaction : totalList){
            totalMonthExpenses=totalMonthExpenses+transaction.getAmount();
        }

    }

    private void bindActivity(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        mTitle = (TextView) view.findViewById(R.id.userNameTextView);
        mTitleContainer = (LinearLayout) view.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.main_appbar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
