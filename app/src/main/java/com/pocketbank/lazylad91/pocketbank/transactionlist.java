package com.pocketbank.lazylad91.pocketbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Category;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class transactionlist extends Fragment {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private static SharedPreferences sharedPref;
    private static String month;
    private static int monthNumber;
    private static String year;
    private static String category;
    private static ArrayList<Transaction> mTransactionList;
    private static ArrayList<Transaction> mTransactionListbackup;
    private TransactionAdapter mAdapter;
    private static Double totalPrice;
    private static String uid;
    private HashMap<String, Transaction> transactionHashMap;
    private RecyclerView recyclerView;
    private static TextView mTotalPriceText;
    private static final int GET_SELECTED_FILTERS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_transactionlist, container, false);
        /* Get current Month and year */
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        if(year == null)
        year=String.valueOf(calendar.get(Calendar.YEAR));
        if(month == null) {
            month = PocketBankConstant.monthMap.get(calendar.get(Calendar.MONTH));
            monthNumber = calendar.get(Calendar.MONTH);
        }Log.d("date",year);
        Log.d("date",month);
        /* Get current Month and year End*/
       /* FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.filterFabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addcategoriesintent = new Intent(getActivity(), PrintTransaction.class);
                startActivityForResult(addcategoriesintent, GET_SELECTED_FILTERS);
            }
        });*/
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) view.findViewById(R.id.main_collapsing);

        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black_overlay));
          mTotalPriceText = (TextView) view.findViewById(R.id.totalPriceText);
        final MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.cateogorySpinner);
        ArrayList<String> categoryString = new ArrayList<>();
        categoryString.add("All");
        category="All";
        spinner.setSelected(true);
        for(Category cat : PocketBankConstant.categoryList){
            categoryString.add(cat.getName());
        }
        spinner.setItems(categoryString);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mTransactionList.clear();
                category=item;
                mTransactionList.addAll(mTransactionListbackup);
                if(!item.equals("All")){
                for (Iterator<Transaction> iterator = mTransactionList.iterator(); iterator.hasNext();) {
                    Transaction txn = iterator.next();
                    if (!txn.getCategory().getName().equals(item)) {
                        // Remove the current element from the iterator and the list.
                        iterator.remove();
                    }
                }
                    Log.d("updatingUI",mTransactionList.size()+"");
                    updateUI();
                    calculateTotalPrice();
            }
                else{
                    updateUI();
                    calculateTotalPrice();
                }
                updateUI();
                calculateTotalPrice();
        }
        });
        final MaterialSpinner monthSpinner = (MaterialSpinner) view.findViewById(R.id.monthSpinner);
        ArrayList<String> monthString = new ArrayList<>();
        monthSpinner.setSelected(true);

            for(int i=0; i<=11; i++) {
                monthString.add(PocketBankConstant.monthMap.get(i));
            }
        monthSpinner.setItems(monthString);
        monthSpinner.setSelected(true);
        monthSpinner.setSelectedIndex(monthNumber);
        monthSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                month=item;
                spinner.setSelectedIndex(0);
                callDb(uid,item,"transactions");
            }
        });


        /*setting visibilty true */
           AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.main_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    Log.d("collapse","print");
                    spinner.setVisibility(View.VISIBLE);
                    monthSpinner.setVisibility(View.VISIBLE);
                    mTotalPriceText.setVisibility(View.VISIBLE);
                    /*
                    collapsingToolbarLayout.setTitle("Title");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black_overlay));*/
                    isShow = true;
                } else if(isShow) {
                    spinner.setVisibility(View.GONE);
                    monthSpinner.setVisibility(View.GONE);
                    spinner.setVisibility(View.INVISIBLE);
                    mTotalPriceText.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
        /*setting visibilty true end */






        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Creating shared preferences code start
        Context context = getContext();
        mTransactionList = new ArrayList<Transaction>();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stopd
        String defaultValue = "null";
         uid = sharedPref.getString("uid", defaultValue);
       callDb(uid,month,"transactions");
        recyclerView = (RecyclerView) view.findViewById(R.id.transaction_recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        updateUI();
        calculateTotalPrice();
        return view;
    }

    private void callDb(String uid,String mnth,String element) {
        mDatabase.child(uid).child(year).child(mnth).child(element).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               /* transactionHashMap =  (HashMap<String,Transaction>)dataSnapshot.getValue();*/
                //int transaction = Log.d("transaction", transactionHashMap.toString());
                mTransactionList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Log.d("keyfire3", messageSnapshot.getChildren().toString());
                    Transaction transaction = new Transaction();
                    Log.d("keyfire3===========", messageSnapshot.child("amount").getValue().toString());

                    if(messageSnapshot.child("amount").exists()){
                        transaction.setAmount(Float.parseFloat(messageSnapshot.child("amount").getValue().toString()));
                    }

                    if(messageSnapshot.child("month").exists()){
                        transaction.setMonth(messageSnapshot.child("month").getValue().toString());
                    }


                    if(messageSnapshot.child("notes").exists()){
                        transaction.setNotes(messageSnapshot.child("notes").getValue().toString());
                    }

                    if(messageSnapshot.child("date").exists()){
                        transaction.setDate(Integer.parseInt(messageSnapshot.child("date").getValue().toString()));
                    }

                    if(messageSnapshot.child("uid").exists()){
                        transaction.setUid(messageSnapshot.child("uid").getValue().toString());
                    }

                    if(messageSnapshot.child("year").exists()){
                        transaction.setYear(Integer.parseInt(messageSnapshot.child("year").getValue().toString()));
                    }

                    if(messageSnapshot.child("category").child("id").exists()){
                        transaction.setCategory(PocketBankConstant.categoryList.get(Integer.parseInt(messageSnapshot.child("category").child("id").getValue().toString())));
                    }



                    Location location = new Location();

                    if(messageSnapshot.child("location").child("lng").exists()){
                        location.setLng(Double.parseDouble(messageSnapshot.child("location").child("lng").getValue().toString()));
                    }

                    if(messageSnapshot.child("location").child("lat").exists()){
                        location.setLat(Double.parseDouble(messageSnapshot.child("location").child("lat").getValue().toString()));
                    }

                    if(messageSnapshot.child("location").child("name").exists()){
                        location.setName(messageSnapshot.child("location").child("name").getValue().toString());
                    }


                    transaction.setLocation(location);
                    mTransactionList.add(transaction);
                }
                mTransactionListbackup = new ArrayList<Transaction>();
                mTransactionListbackup.addAll(mTransactionList);
                updateUI();
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
        for(Transaction transaction : mTransactionList){
            totalPrice=totalPrice+transaction.getAmount();
        }
        mTotalPriceText.setText("  $"+totalPrice);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String  selectedPath;
        switch(requestCode) {

            case 1:    // Result from Categories Intent

                if (resultCode == Activity.RESULT_OK) {
                   /* Bundle bundle = data.getExtras();
                    selectedCategory = (Category) bundle.getSerializable("category");
                    categoryedittext.setText(selectedCategory.getName());
                    mcategoryImageView.setImageResource(getResources().getIdentifier(selectedCategory.getImage(), "drawable", getPackageName()));
             */   }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        calculateTotalPrice();
    }

    private void updateUI() {
        if (mTransactionList != null) {
            Log.d("mTransactionList", String.valueOf(mTransactionList.size()));
            List<Transaction> transactions = mTransactionList;
            Collections.reverse(transactions);
            Log.d("recyclerview", "insideadpater");
            /*if (mAdapter == null) {*/
            Log.d("recyclerview1", mTransactionList.size()+"");
            mAdapter = new TransactionAdapter(mTransactionList);
            recyclerView.setAdapter(mAdapter);
            /*} *//*else {
                Log.d("recyclerview","insideadpatersetchanged");
                mAdapter.notifyDataSetChanged();
            }*/
        }
    }

    private class TransactionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mDateTextView;
        private TextView mRateTextView;
        private ImageView mCategoryImage;

        private Transaction mTransaction;

        public TransactionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mDateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            mRateTextView = (TextView) itemView.findViewById(R.id.rateTextView);
            mCategoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
        }

        public void bindTransaction(Transaction transaction) {
            mTransaction = transaction;
            //Log.d("nameof",restaurant.getName());
            mRateTextView.setText("$" + String.valueOf(transaction.getAmount()));
            mDateTextView.setText(mTransaction.getDate() + " " + month + " " + year);
            mCategoryImage.setImageResource(getResources().getIdentifier(mTransaction.getCategory().getImage(), "drawable", getActivity().getPackageName()));

        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), transactionDetail.class);
            intent.putExtra("transaction",mTransaction);
            startActivity(intent);
        }


    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder> {
        private List<Transaction> mTransactions;

        public TransactionAdapter(List<Transaction> transactions) {
            mTransactions = transactions;
        }


        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_transactions, parent, false);
            return new TransactionHolder(view);
        }


        @Override
        public void onBindViewHolder(TransactionHolder holder, int position) {
            // Log.d("type1",mTransactions.get(position).getClass().getName());
            Transaction transaction = (Transaction) mTransactions.get(position);
            holder.bindTransaction(transaction);
        }


        @Override
        public int getItemCount() {
            return mTransactions.size();
        }

    }
}
