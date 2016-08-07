package com.pocketbank.lazylad91.pocketbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketbank.lazylad91.pocketbank.Model.Transaction;
import com.squareup.picasso.Picasso;

public class transactionDetail extends AppCompatActivity {

   private static Transaction mTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        Intent i = getIntent();
        if(i.hasExtra("transaction")) {
            mTransaction = (Transaction) i.getSerializableExtra("transaction");
        }
        if(mTransaction!=null){
            /*TextView paymenttype = (TextView) findViewById(R.id.paymenttype);*/
            TextView money = (TextView) findViewById(R.id.money);
            TextView date = (TextView) findViewById(R.id.date);
            TextView categoryTextView = (TextView) findViewById(R.id.categoryTextView);
            TextView addresstext = (TextView) findViewById(R.id.addresstext);
            TextView note = (TextView) findViewById(R.id.note);
            ImageView transactionImageView = (ImageView) findViewById(R.id.transactionImageView);

           /* if(mTransaction.getPaymentMode()!=null)
            paymenttype.setText(mTransaction.getPaymentMode().getCardType());*/
            money.setText(mTransaction.getAmount()+"$");
            date.setText(mTransaction.getDate()+" "+ mTransaction.getMonth()+" "+mTransaction.getYear());
            categoryTextView.setText(mTransaction.getCategory().getName());
            addresstext.setText(mTransaction.getLocation().getName());
            note.setText(mTransaction.getNotes());
            String url = "https://maps.googleapis.com/maps/api/staticmap?center=San+jose,CA&zoom=13&size=600x300&maptype=roadmap&markers=color:blue|\"+mTransaction.getLocation().getLat()+\",\"+mTransaction.getLocation().getLng()+\"&key=AIzaSyAKXBE_pTVfs4LYlwPW3KCoqdGofayz9Ug";
            Picasso.with(transactionDetail.this)
                    .load(url)
                    .fit()
                    .into(transactionImageView);

        }
    }
}
