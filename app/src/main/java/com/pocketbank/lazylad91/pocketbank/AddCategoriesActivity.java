package com.pocketbank.lazylad91.pocketbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddCategoriesActivity extends AppCompatActivity implements View.OnClickListener{
static String chosenCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
//
//        final EditText restaurantcategory = (EditText) findViewById (R.id.categoryrestaurants);
//        restaurantcategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AddCategoriesActivity.this, restaurantcategory.getText().toString(), Toast.LENGTH_LONG).show();
//                returnDataToTransactionActivity(restaurantcategory.getText().toString());
//            }
//        });


        //returnDataToTransactionActivity(chosenCategory);
    }

    private void returnDataToTransactionActivity(String selectedCategory) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selectedCategory",selectedCategory);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }


    @Override
    public void onClick(View view) {
        View.OnClickListener myOnlyhandler = new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("getId",String.valueOf(v.getId()));
                switch(v.getId()) {
//                    case R.id.b1:
//                        // it was the first button
//                        break;
//                    case R.id.b2:
//                        // it was the second button
//                        break;

                }
            }
        };
    }
}
