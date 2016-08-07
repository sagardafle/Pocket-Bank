package com.pocketbank.lazylad91.pocketbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;

import java.util.HashMap;
import java.util.Map;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

public class BudgetActivity extends AppCompatActivity {
    private TextView seekbarvalue;
    static int shoppingvalue = 0, restaurantvalue = 0, travelvalue = 0, savingvalue = 0, totalvalueset = 0, totalbudget = 1000, remainingBudget = 1000, changedValue = 0, grocerystep = 100, entertainmentstep = 100, restaurantstep = 100, savingsstep = 100, salarystep = 500;
    SeekBarCompat salaryseekbarCompat, shoppingSeekBarCompat, travelSeekBarCompat, restaurantseekbarCompat, savingsseekbarCompat;
    private TextView salarydisplayvalue, shoppingdisplayvalue, traveldisplayvalue, restaurantdisplayvalue, savingsdisplayvalue;
    private static DatabaseReference mDatabase;
     CircularSeekBar mycircularseekbar;
    private static HashMap<String,String> resultMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mycircularseekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        setSeekBars();


        /**
         * Disable all the spinners except salary
         */

        shoppingSeekBarCompat.setEnabled(false);
        travelSeekBarCompat.setEnabled(false);
        restaurantseekbarCompat.setEnabled(false);
        savingsseekbarCompat.setEnabled(false);


        seekbarvalue = (TextView) findViewById(R.id.seekbarvalue);

        salarydisplayvalue = (TextView) findViewById(R.id.salarydisplayimage);
        shoppingdisplayvalue = (TextView) findViewById(R.id.shoppingdisplayimage);
        traveldisplayvalue = (TextView) findViewById(R.id.traveldisplayimage);
        restaurantdisplayvalue = (TextView) findViewById(R.id.restaurantdisplayimage);
        savingsdisplayvalue = (TextView) findViewById(R.id.savingsdisplayimage);


        /**
         * Setting the circular seekbar
         */


        // mycircularseekbar.setMax(1000);


        /**
         *  Listener for salary seekbar
         */

        salaryseekbarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // salarydisplayvalue.setText(String.valueOf(seekBar.getProgress()));

                progress = ((int) Math.round(progress / salarystep)) * salarystep;
                seekBar.setProgress(progress);
                salarydisplayvalue.setText(progress + "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                /**
                 * Enable all the spinners
                 */

                shoppingSeekBarCompat.setEnabled(true);
                travelSeekBarCompat.setEnabled(true);
                restaurantseekbarCompat.setEnabled(true);
                savingsseekbarCompat.setEnabled(true);


                totalbudget = seekBar.getProgress();
                remainingBudget = totalbudget - shoppingvalue - restaurantvalue - travelvalue - savingvalue;
                seekbarvalue.setText(remainingBudget + "");
                shoppingSeekBarCompat.setMax(totalbudget);
                travelSeekBarCompat.setMax(totalbudget);
                restaurantseekbarCompat.setMax(totalbudget);
                savingsseekbarCompat.setMax(totalbudget);
                mycircularseekbar.setMax(totalbudget);
            }
        });


        /**
         *  Listener for grocery seekbar
         */

        shoppingSeekBarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progress = ((int) Math.round(progress / grocerystep)) * grocerystep;
                seekBar.setProgress(progress);
                shoppingdisplayvalue.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changedValue = seekBar.getProgress();
                if (findValid(changedValue, shoppingvalue)) {
                    remainingBudget = remainingBudget - (changedValue - shoppingvalue);
                    shoppingvalue = changedValue;
                    seekbarvalue.setText("Remaining budget: " + remainingBudget + "");
                    //mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue);
                    mycircularseekbar.setProgress(totalbudget - remainingBudget);
                } else {
                    seekBar.setProgress(shoppingvalue);
                    Toast toast = Toast.makeText(BudgetActivity.this, "You can not set more than your salary",
                            Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        });


        /**
         * Listener for the enterntainment seekbar
         */

        travelSeekBarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progress = ((int) Math.round(progress / entertainmentstep)) * entertainmentstep;
                seekBar.setProgress(progress);
                traveldisplayvalue.setText(progress + "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changedValue = seekBar.getProgress();

                if (findValid(changedValue, travelvalue)) {
                    remainingBudget = remainingBudget - (changedValue - travelvalue);
                    travelvalue = changedValue;
                    seekbarvalue.setText("Remaining budget " + remainingBudget + "");

                    //mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue);
                    mycircularseekbar.setProgress(totalbudget - remainingBudget);
                } else {
                    seekBar.setProgress(travelvalue);
                    Toast toast = Toast.makeText(BudgetActivity.this, "You can not set more than your salary",
                            Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            }
        });


        /**
         * Listener for the restaurant seekbar
         */

        restaurantseekbarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int) Math.round(progress / restaurantstep)) * restaurantstep;
                seekBar.setProgress(progress);
                restaurantdisplayvalue.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                changedValue = seekBar.getProgress();
                if (findValid(changedValue, restaurantvalue)) {
                    remainingBudget = remainingBudget - (changedValue - restaurantvalue);
                    restaurantvalue = changedValue;
                    seekbarvalue.setText("Remaining budget " + remainingBudget + "");
                    //mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue);
                    mycircularseekbar.setProgress(totalbudget - remainingBudget);
                } else {
                    seekBar.setProgress(restaurantvalue);
                    Toast toast = Toast.makeText(BudgetActivity.this, "You can not set more than your salary",
                            Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            }

        });


        /**
         * Listener for the savings seekbar
         */

        savingsseekbarCompat.setOnSeekBarChangeListener(new SeekBarCompat.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int) Math.round(progress / savingsstep)) * savingsstep;
                seekBar.setProgress(progress);
                savingsdisplayvalue.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changedValue = seekBar.getProgress();
                if (findValid(changedValue, savingvalue)) {
                    remainingBudget = remainingBudget - (changedValue - savingvalue);
                    savingvalue = changedValue;
                    seekbarvalue.setText(remainingBudget + "");
                    //mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue);
                    mycircularseekbar.setProgress(totalbudget - remainingBudget);
                } else {
                    seekBar.setProgress(savingvalue);
                    Toast toast = Toast.makeText(BudgetActivity.this, "You can not save more than leftover data",
                            Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            }

        });
        checkIfValuesExist();

    }

    public  void checkIfValuesExist() {
        SharedPreferences sharedPref;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth;
        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stopd
        String defaultValue = "null";
        String uid = sharedPref.getString("uid", defaultValue);
        initializeData(uid);



    }

    private void initializeData(String uid) {
        mDatabase.child(uid).child("Budget").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("DataSnapshot contents",dataSnapshot.toString() );
                    Log.d("initializeData",s+""+"onChildAdded");
                        resultMap.put(dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
                        Log.d("resultMap",resultMap.toString());
                        if(resultMap.containsKey("4")) {
                            shoppingSeekBarCompat.setEnabled(true);
                            shoppingSeekBarCompat.setProgress(Integer.parseInt(resultMap.get("4")));
                            shoppingdisplayvalue.setText(resultMap.get("4"));
                        }
                if(resultMap.containsKey("0")){
                    restaurantseekbarCompat.setEnabled(true);
                    restaurantseekbarCompat.setProgress(Integer.parseInt(resultMap.get("0")));
                    restaurantdisplayvalue.setText(resultMap.get("0"));
                        }
               if(resultMap.containsKey("6")) {
                   travelSeekBarCompat.setEnabled(true);
                   travelSeekBarCompat.setProgress(Integer.parseInt(resultMap.get("6")));
                   traveldisplayvalue.setText(resultMap.get("6"));
               }
                if(resultMap.containsKey("Salary")){
                    salarydisplayvalue.setText(resultMap.get("Salary"));
                    salaryseekbarCompat.setProgress(Integer.parseInt(resultMap.get("Salary")));
                    travelSeekBarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    totalbudget=Integer.parseInt(resultMap.get("Salary"));
                    savingsseekbarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    restaurantseekbarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    shoppingSeekBarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    mycircularseekbar.setMax(totalbudget);
                }
                if(resultMap.containsKey("Savings")){
                    savingvalue = Integer.parseInt(resultMap.get("Savings"));
                    savingsseekbarCompat.setEnabled(true);
                    savingsseekbarCompat.setProgress(savingvalue);
                    savingsdisplayvalue.setText(savingvalue+"");
                }
                mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue+savingvalue);
                seekbarvalue.setText("Remaining budget: " + remainingBudget + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("initializeData",s+""+"onChildChanged");
                Log.d("DataSnapshot contents",dataSnapshot.toString() );
                Log.d("initializeData",s+""+"onChildAdded");
                resultMap.put(dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
                Log.d("resultMap",resultMap.toString());
                if(resultMap.containsKey("4")) {
                    shoppingSeekBarCompat.setEnabled(true);
                    shoppingSeekBarCompat.setProgress(Integer.parseInt(resultMap.get("4")));
                    shoppingdisplayvalue.setText(resultMap.get("4"));
                }
                if(resultMap.containsKey("0")){
                    restaurantseekbarCompat.setEnabled(true);
                    restaurantseekbarCompat.setProgress(Integer.parseInt(resultMap.get("0")));
                    restaurantdisplayvalue.setText(resultMap.get("0"));
                }
                if(resultMap.containsKey("6")) {
                    travelSeekBarCompat.setEnabled(true);
                    travelSeekBarCompat.setProgress(Integer.parseInt(resultMap.get("6")));
                    traveldisplayvalue.setText(resultMap.get("6"));
                }
                if(resultMap.containsKey("Salary")){
                    salarydisplayvalue.setText(resultMap.get("Salary"));
                    salaryseekbarCompat.setProgress(Integer.parseInt(resultMap.get("Salary")));
                    travelSeekBarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    totalbudget=Integer.parseInt(resultMap.get("Salary"));
                    savingsseekbarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    restaurantseekbarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    shoppingSeekBarCompat.setMax(Integer.parseInt(resultMap.get("Salary")));
                    mycircularseekbar.setMax(totalbudget);
                }
                if(resultMap.containsKey("Savings")){
                    savingvalue = Integer.parseInt(resultMap.get("Savings"));
                    savingsseekbarCompat.setEnabled(true);
                    savingsseekbarCompat.setProgress(savingvalue);
                    savingsdisplayvalue.setText(savingvalue+"");
                }
                mycircularseekbar.setProgress(shoppingvalue+travelvalue+restaurantvalue+savingvalue);
                seekbarvalue.setText("Remaining budget: " + remainingBudget + "");
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


    private void setSeekBars() {

        salaryseekbarCompat = (SeekBarCompat) findViewById(R.id.salaryseekbar);
        salaryseekbarCompat.setThumbColor(Color.YELLOW);
        salaryseekbarCompat.setProgressColor(Color.GREEN);
        salaryseekbarCompat.setProgressBackgroundColor(Color.RED);
        salaryseekbarCompat.setThumbAlpha(128);
        salaryseekbarCompat.setMax(10000);


        shoppingSeekBarCompat = (SeekBarCompat) findViewById(R.id.shoppingseekbar);
        shoppingSeekBarCompat.setThumbColor(Color.MAGENTA);
        shoppingSeekBarCompat.setProgressColor(Color.LTGRAY);
        shoppingSeekBarCompat.setProgressBackgroundColor(Color.GREEN);
        shoppingSeekBarCompat.setThumbAlpha(128);
        shoppingSeekBarCompat.setMax(totalbudget);

        travelSeekBarCompat = (SeekBarCompat) findViewById(R.id.travelseekbar);
        travelSeekBarCompat.setThumbColor(Color.RED);
        travelSeekBarCompat.setProgressColor(Color.MAGENTA);
        travelSeekBarCompat.setProgressBackgroundColor(Color.BLUE);
        travelSeekBarCompat.setThumbAlpha(128);
        travelSeekBarCompat.setMax(totalbudget);

        restaurantseekbarCompat = (SeekBarCompat) findViewById(R.id.restaurantseekbar);
        restaurantseekbarCompat.setThumbColor(Color.YELLOW);
        restaurantseekbarCompat.setProgressColor(Color.CYAN);
        restaurantseekbarCompat.setProgressBackgroundColor(Color.GREEN);
        restaurantseekbarCompat.setThumbAlpha(128);
        restaurantseekbarCompat.setMax(totalbudget);


        savingsseekbarCompat = (SeekBarCompat) findViewById(R.id.savingsseekbar);
        savingsseekbarCompat.setThumbColor(Color.MAGENTA);
        savingsseekbarCompat.setProgressColor(Color.LTGRAY);
        savingsseekbarCompat.setProgressBackgroundColor(Color.GREEN);
        savingsseekbarCompat.setThumbAlpha(128);
        savingsseekbarCompat.setMax(totalbudget);


    }

    private Boolean findValid(int changedValue, int previousValue) {
        if (remainingBudget >= changedValue - previousValue) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.savebudget, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.savebudget) {
            Log.d("Save Budget", "pressed");
            saveUserBudget();
        }

        if(id == android.R.id.home){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUserBudget() {
        Context context = getApplicationContext();
        String defaultValue = "null";
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = sharedPref.getString("uid", defaultValue);
        Log.d("uid", uid);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("Salary",totalbudget);
        childUpdates.put("Savings",savingvalue);
        Log.d("finaltesting",shoppingvalue+"");
        Log.d("finaltesting",travelvalue+"");
        Log.d("finaltesting",restaurantvalue+"");
        childUpdates.put(String.valueOf(PocketBankConstant.clothing.getId()), shoppingvalue);
        childUpdates.put(String.valueOf(PocketBankConstant.traveller.getId()), travelvalue);
        childUpdates.put(String.valueOf(PocketBankConstant.restaurant.getId()), restaurantvalue);

       /* childUpdates.put("Saving",savingvalue);
        childUpdates.put("Salary",totalbudget);*/
        //childUpdates.put(uid + "/" + "Salary", totalbudget);

    /*    Map<String, Object> salarymap = new HashMap<>();
        salarymap.put("Salary",totalbudget);
        salarymap.put("Saving",savingvalue);*/
        mDatabase.child(uid).child("Budget").updateChildren(childUpdates);
        /*mDatabase.child(uid).child("Salary").setValue(totalbudget);
        mDatabase.child(uid).child("Savings").setValue(savingvalue);*/

        Toast toast = Toast.makeText(this, "Budget saved !", Toast.LENGTH_LONG);
        toast.show();


    }

}

