package com.pocketbank.lazylad91.pocketbank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class transactionlist extends Fragment {

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private static SharedPreferences sharedPref;
    private static String month = "June";
    private static String year = "2016";
    private static ArrayList<Transaction> mTransactionList;
    private TransactionAdapter mAdapter;
    private HashMap<String, Transaction> transactionHashMap;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_transactionlist, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Creating shared preferences code start
        Context context = getContext();
        mTransactionList = new ArrayList<Transaction>();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stopd
        String defaultValue = "null";
        String uid = sharedPref.getString("uid", defaultValue);
        mDatabase.child(uid).child(year).child(month).child("transactions").addValueEventListener(new ValueEventListener() {
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
                    transaction.setAmount(Float.parseFloat(messageSnapshot.child("amount").getValue().toString()));
                    transaction.setMonth(messageSnapshot.child("month").getValue().toString());
                    transaction.setNotes(messageSnapshot.child("notes").getValue().toString());
                    transaction.setDate(Integer.parseInt(messageSnapshot.child("date").getValue().toString()));
                    transaction.setUid(messageSnapshot.child("uid").getValue().toString());
                    transaction.setYear(Integer.parseInt(messageSnapshot.child("year").getValue().toString()));
                    transaction.setCategory(PocketBankConstant.categoryList.get(Integer.parseInt(messageSnapshot.child("category").child("id").getValue().toString())));
                    Location location = new Location();
                    location.setLng(Double.parseDouble(messageSnapshot.child("location").child("lng").getValue().toString()));
                    location.setLat(Double.parseDouble(messageSnapshot.child("location").child("lat").getValue().toString()));
                    location.setName(messageSnapshot.child("location").child("name").getValue().toString());
                    transaction.setLocation(location);
                    mTransactionList.add(transaction);
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("keyFiredCancel", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.transaction_recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        updateUI();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (mTransactionList != null && mTransactionList.size() != 0) {
            Log.d("mTransactionList", String.valueOf(mTransactionList.size()));
            List<Transaction> transactions = mTransactionList;
            Log.d("recyclerview", "insideadpater");
            /*if (mAdapter == null) {*/
            Log.d("recyclerview", "insideadpaternull");
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
            mRateTextView.setText(String.valueOf(transaction.getAmount()));
            mDateTextView.setText(mTransaction.getDate() + " " + month + " " + year);
            mCategoryImage.setImageResource(getResources().getIdentifier(mTransaction.getCategory().getImage(), "drawable", getActivity().getPackageName()));

        }


        @Override
        public void onClick(View v) {/*
            Intent intent = new Intent(getActivity(), ExpenseDetail.class);
            intent.putExtra("Expense_Id",mExpense.getId());
            startActivity(intent);

            Uri gmmIntentUri = Uri.parse("google.navigation:q="+mRestaurant.getLat()+","+mRestaurant.getLng()+"&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(mapIntent);

            }*/
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
