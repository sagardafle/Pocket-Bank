package com.pocketbank.lazylad91.pocketbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddTransactionActivity extends AppCompatActivity  {

    TextView placepicker;
    private static final int REQUEST_PLACE_PICKER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        placepicker = (TextView) findViewById(R.id.transactionplaces);
        placepicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(intentBuilder.build(AddTransactionActivity.this), REQUEST_PLACE_PICKER);
                    }
                catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_PLACE_PICKER) {
                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(data, AddTransactionActivity.this);
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
                        placepicker.setText(place.getName());
                    }
                }
            }
}
