package com.pocketbank.lazylad91.pocketbank;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.PaymentMode;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class chartFragment extends Fragment {


    private static SharedPreferences sharedPref;
    private static BarChart chart;
    private static String month;
    private static int monthNumber;
    private static String year;
    private static String category;
    private static Double totalPrice=0.0;
    private static String uid;
    private static ArrayList<Transaction> mTransactionList;
    private static Double cashTotal=0.0;
    private static Double creditTotal=0.0;
    private static Double debitTotal=0.0;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    public chartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chart, container, false);
         /* Get current Month and year */
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if(year == null)
            year=String.valueOf(calendar.get(Calendar.YEAR));
        if(month == null) {
            month = PocketBankConstant.monthMap.get(calendar.get(Calendar.MONTH));
            monthNumber = calendar.get(Calendar.MONTH);
        }
        sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stopd
        String defaultValue = "null";
        chart = (BarChart) view.findViewById(R.id.chart);
        uid = sharedPref.getString("uid", defaultValue);
        mTransactionList= new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        calldb(uid,month,"transactions");
        return view;
    }

    private void calldb(String uid,String mnth,String element) {
        mDatabase.child(uid).child(year).child(mnth).child(element).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTransactionList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Log.d("keyfire3", messageSnapshot.getChildren().toString());
                    Transaction transaction = new Transaction();
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

                    PaymentMode paymentMode = new PaymentMode();
                    if(messageSnapshot.child("paymentMode").child("cardType").exists()) {
                        paymentMode.setCardType(messageSnapshot.child("paymentMode").child("cardType").getValue().toString());
                    }

                    if(messageSnapshot.child("paymentMode").child("cardNumber").exists()) {
                        location.setLat(Double.parseDouble(messageSnapshot.child("paymentMode").child("cardNumber").getValue().toString()));
                        paymentMode.setCardId(Integer.parseInt(messageSnapshot.child("paymentMode").child("cardNumber").getValue().toString()));
                    }

                    if(messageSnapshot.child("paymentMode").child("cardId").exists()) {
                        paymentMode.setCardId(Integer.parseInt(messageSnapshot.child("paymentMode").child("cardId").getValue().toString()));
                    }
                    transaction.setPaymentMode(paymentMode);
                    transaction.setLocation(location);
                    mTransactionList.add(transaction);
                    calculateTotalPrice();
                }
                calculateTotalPrice();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("keyFiredCancel", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        calculateTotalPrice();

    }

    private void calculateTotalPrice() {
        totalPrice=0.0;
        cashTotal=0.0;
        debitTotal=0.0;
        creditTotal=0.0;
        for(Transaction transaction : mTransactionList){
            totalPrice=totalPrice+transaction.getAmount();

            if(transaction.getPaymentMode().getCardId()==1)
                cashTotal=cashTotal+transaction.getAmount();
            if(transaction.getPaymentMode().getCardId()==2)
                debitTotal=debitTotal+transaction.getAmount();
            if(transaction.getPaymentMode().getCardId()==3)
                creditTotal=creditTotal+transaction.getAmount();
        }



        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(cashTotal.floatValue(), 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(debitTotal.floatValue(), 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(creditTotal.floatValue(), 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(totalPrice.floatValue(), 3); // Mar
        valueSet1.add(v1e4);
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Payment Type");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Cash");
        xAxis.add("Debit");
        xAxis.add("Credit");
        xAxis.add("Total");
        return xAxis;
    }
}
