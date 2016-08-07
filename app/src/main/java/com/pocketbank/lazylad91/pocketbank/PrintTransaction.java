package com.pocketbank.lazylad91.pocketbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Category;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;
import com.pocketbank.lazylad91.pocketbank.Services.PrintService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

public class PrintTransaction extends AppCompatActivity {

    private static String year;
    private static String month;
    private static int monthNumber;
    private static String selectedCategory;
    private static ArrayList<Transaction> txnList;
    private static ArrayList<Transaction> backupTxnList;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private static SharedPreferences sharedPref;
    private static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_transaction);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Creating shared preferences code start
        txnList = new ArrayList<Transaction>();
        backupTxnList = new ArrayList<>();
        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = "null";
        uid = sharedPref.getString("uid", defaultValue);
        // creating shared preferences code stopd


        final MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.cateogorySpinner);
        final MaterialSpinner monthSpinner = (MaterialSpinner) findViewById(R.id.monthSpinner);
        final MaterialSpinner yearSpinner = (MaterialSpinner) findViewById(R.id.yearSpinner);
           /* Get current Month and year */
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if(year == null)
            year=String.valueOf(calendar.get(Calendar.YEAR));
        if(month == null) {
            month = PocketBankConstant.monthMap.get(calendar.get(Calendar.MONTH));
            monthNumber = calendar.get(Calendar.MONTH);
        }
        Log.d("date",year);
        Log.d("date",month);
        /* Get current Month and year End*/

        ArrayList<String> monthString = new ArrayList<>();
        monthSpinner.setSelected(true);

        for(int i=0; i<=11; i++) {
            monthString.add(PocketBankConstant.monthMap.get(i));
        }
        monthSpinner.setItems(monthString);
        yearSpinner.setItems("2014","2015","2016","2017","2018");

        ArrayList<String> categoryString = new ArrayList<>();
        categoryString.add("All");
            for(Category cat : PocketBankConstant.categoryList){
                categoryString.add(cat.getName());
            }
            spinner.setItems(categoryString);
        monthSpinner.setSelectedIndex(monthNumber);
        yearSpinner.setSelectedIndex(2);
        spinner.setSelectedIndex(0);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                selectedCategory=item;
                txnList.clear();
                txnList.addAll(backupTxnList);
                if(!item.equals("All")){
                    for (Iterator<Transaction> iterator = txnList.iterator(); iterator.hasNext();) {
                        Transaction txn = iterator.next();
                        if (!txn.getCategory().getName().equals(item)) {
                            // Remove the current element from the iterator and the list.
                            iterator.remove();
                        }
                    }
                }
            }
        });
        monthSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                month=item;
                txnList.clear();
                backupTxnList.clear();
                spinner.setSelectedIndex(0);
                callDb(uid,month,"transactions", year);
            }
        });
        yearSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                year=item;
                txnList.clear();
                backupTxnList.clear();
                spinner.setSelectedIndex(0);
                callDb(uid,month,"transactions",year);
            }
        });
        Button btn = (Button)findViewById(R.id.generatePDF);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = PrintService.createFolder(txnList);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                viewPdf(file);
            }
        });
        callDb(uid,month,"transactions", year);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callDb(String uid, String mnth, String element, String year) {
        mDatabase.child(uid).child(PrintTransaction.year).child(mnth).child(element).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               /* transactionHashMap =  (HashMap<String,Transaction>)dataSnapshot.getValue();*/
                //int transaction = Log.d("transaction", transactionHashMap.toString());
                txnList.clear();
                backupTxnList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Log.d("keyfire3", messageSnapshot.getChildren().toString());
                    Transaction transaction = new Transaction();
                    Log.d("keyfire3===========", messageSnapshot.child("amount").getValue().toString());


                    if(messageSnapshot.child("amount").exists() && messageSnapshot.child("amount").getValue() !=null) {
                        transaction.setAmount(Float.parseFloat(messageSnapshot.child("amount").getValue().toString()));
                    }

                    if(messageSnapshot.child("month").exists() && messageSnapshot.child("month").getValue() !=null){
                        transaction.setMonth(messageSnapshot.child("month").getValue().toString());
                    }


                    if(messageSnapshot.child("notes").exists() && messageSnapshot.child("notes").getValue() != null){
                        transaction.setNotes(messageSnapshot.child("notes").getValue().toString());
                    }

                    if(messageSnapshot.child("date").exists() && messageSnapshot.child("date").getValue() != null){
                        transaction.setDate(Integer.parseInt(messageSnapshot.child("date").getValue().toString()));
                    }

                    if(messageSnapshot.child("uid").exists() && messageSnapshot.child("uid").getValue() != null){
                        transaction.setUid(messageSnapshot.child("uid").getValue().toString());
                    }

                    if(messageSnapshot.child("year").exists() && messageSnapshot.child("year").getValue().toString() != null){
                        transaction.setYear(Integer.parseInt(messageSnapshot.child("year").getValue().toString()));
                    }

                    if(messageSnapshot.child("category").exists() && messageSnapshot.child("category").child("id").getValue() != null){
                        transaction.setCategory(PocketBankConstant.categoryList.get(Integer.parseInt(messageSnapshot.child("category").child("id").getValue().toString())));
                    }

                    Location location = new Location();

                    if(messageSnapshot.child("location").child("lng").exists() && messageSnapshot.child("location").child("lng").getValue() != null){
                        location.setLng(Double.parseDouble(messageSnapshot.child("location").child("lng").getValue().toString()));
                    }

                    if(messageSnapshot.child("location").child("lat").exists() &&messageSnapshot.child("location").child("lat").getValue()!=null){
                        location.setLat(Double.parseDouble(messageSnapshot.child("location").child("lat").getValue().toString()));
                    }

                    if((messageSnapshot.child("location").child("name").getValue() !=null)){
                        location.setName(messageSnapshot.child("location").child("name").getValue().toString());
                    }

                    transaction.setLocation(location);
                    txnList.add(transaction);
                }
                backupTxnList.addAll(txnList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("keyFiredCancel", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

      }

    private void viewPdf(File myFile){
        if(myFile!=null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else{
            Toast.makeText(PrintTransaction.this,"No Summary found",Toast.LENGTH_SHORT);
        }
    }
}
