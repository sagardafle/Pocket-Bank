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

public class AddCategoriesActivity extends Activity {
static String chosenCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);



        //returnDataToTransactionActivity(chosenCategory);
    }

            public void onClick(View v) {
                Log.d("Clicked" ,String.valueOf(v.getId()));
                EditText pressededittext = (EditText)v;
                chosenCategory = pressededittext.getText().toString();
                returnDataToTransactionActivity(chosenCategory);
            }


    private void returnDataToTransactionActivity(String selectedCategory) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selectedCategory",selectedCategory);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    @Override
    public void onResume(){
        super.onResume();
    }

}


