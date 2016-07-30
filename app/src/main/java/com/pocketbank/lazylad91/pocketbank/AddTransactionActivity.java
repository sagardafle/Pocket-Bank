package com.pocketbank.lazylad91.pocketbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Category;
import com.pocketbank.lazylad91.pocketbank.Model.Location;
import com.pocketbank.lazylad91.pocketbank.Model.PaymentMode;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;

public class AddTransactionActivity extends AppCompatActivity  {

    TextView placepicker;
    ImageButton uploadimagebtn;
    LinearLayout loadmorelayout;
    Intent takePicture;
    ImageView uploadedimage;
    EditText categoryedittext;
    ImageView mcategoryImageView;
    LinearLayout placeslayout,imageslayout,spinnerlayout ;
    ImageButton uploadedgalleryimage;
    private EditText mTransactionAmount;
    private EditText mTransactioNotes;
    private static Category selectedCategory;
    public static int tYear;
    public static int tMonth;
    public static int tDate;
    private static PaymentMode selectedPaymentMode;
    private static Location selectedLocation;


/*    private String mUserId;
    private String transactionUrl;
    private  static Firebase mRef;*/

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private static SharedPreferences sharedPref;

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_GALLERY_IMAGE_CAPTURE = 1;
    private static final int GET_SELECTED_CATEGORY = 2;
    private static final int REQUEST_PLACE_PICKER = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        mTransactionAmount = (EditText) findViewById(R.id.transactionamount);
        mTransactioNotes = (EditText) findViewById(R.id.transactionotes);

        /**
         * COde to load the google place picker
         */
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

/**
 * Code to load the images ia gallery and the camera.
 */
        uploadimagebtn = (ImageButton) findViewById(R.id.transactioncamerauploadimage);
        uploadedimage = (ImageView) findViewById(R.id.transactiodisplayimage);
        uploadedgalleryimage = (ImageButton) findViewById(R.id.transactiongalleryuploadimage);
        uploadimagebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                }

            }

        });


        uploadedgalleryimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select file to upload "), REQUEST_GALLERY_IMAGE_CAPTURE);

            }

        });
        loadmorelayout = (LinearLayout) findViewById(R.id.loadmoredata) ;
        TextView loadmore = (TextView) findViewById(R.id.loadmorefields);
        ImageView loadmoreicon = (ImageView)  findViewById(R.id.dropdownlist);
        placeslayout = (LinearLayout)  findViewById(R.id.placeslayout);
        imageslayout = (LinearLayout) findViewById(R.id.imageslayouts);
        spinnerlayout = (LinearLayout) findViewById(R.id.spinnerlayout);
        placeslayout.setVisibility(View.GONE);

        loadmore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadotherfields();
            }
        });

        loadmoreicon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadotherfields();
            }
        });


        /**
         *
         * Data to load the spinner
         */


        Spinner spinner = (Spinner) findViewById(R.id.cards_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cards_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        /**
         * Listener for adding the categories.
         */

        categoryedittext = (EditText) findViewById(R.id.transactioncategory);
        mcategoryImageView = (ImageView) findViewById(R.id.categoryImage);
        categoryedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent addcategoriesintent = new Intent(AddTransactionActivity.this, AddCategoriesActivity.class);
                    startActivityForResult(addcategoriesintent, GET_SELECTED_CATEGORY);
                    return true; // return is important...
                }
                return true;
            }
        });


        // Firebase code start //
        /*try {
            mRef = new Firebase(Constant.FIREBASE_URL);
        } catch (Exception e) {
            Log.d("FirebaseConnection","Unable to connect to firebase");
        }
*/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Firebase code ending //

        // Creating shared preferences code start
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stop
    }

    /**
     * Shows fields when user clicks on add more values
     */
    private void loadotherfields() {
        loadmorelayout.setVisibility(View.GONE);
        placeslayout.setVisibility(View.VISIBLE);
        imageslayout.setVisibility(View.VISIBLE);
    }


    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String  selectedPath;
        switch(requestCode) {
            case 0:  // Result from CAMERA activity
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadedimage.setImageBitmap(imageBitmap);
                }
                break;

            case 1: // Result from Gallery activity
                if (resultCode == RESULT_OK) {

                    if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        selectedPath = getPath(selectedImageUri);
                        uploadedimage.setImageURI(selectedImageUri);
//                                Log.d("selectedPath1 : " ,selectedPath);
                    } else {
                        Log.d("selectedPath1 : ", "Came here its null !");
                    }
                }
                break;

            case 100:  // Result from Google PlacePicker Intent
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, AddTransactionActivity.this);
                    selectedLocation = new Location();
                    selectedLocation.setLng(place.getLatLng().longitude);
                    selectedLocation.setLat(place.getLatLng().latitude);
                    selectedLocation.setName(place.getName().toString());
//                    String toastMsg = String.format("Place: %s", place.getName());
//                    Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
                    Log.d("LatLng", place.getLatLng().toString());
                    placepicker.setText(place.getName());
                }
                break;

            case 2:    // Result from Categories Intent

                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    selectedCategory = (Category) bundle.getSerializable("category");
                    categoryedittext.setText(selectedCategory.getName());
                    mcategoryImageView.setImageResource(getResources().getIdentifier(selectedCategory.getImage(), "drawable", getPackageName()));
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }

        }
    }


    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.saveexpense,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveexpense) {
            saveTransaction();

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTransaction() {
        Transaction transaction = new Transaction();
        String selectedMonth = PocketBankConstant.monthMap.get(tMonth);
        transaction.setAmount(Float.parseFloat(mTransactionAmount.getText().toString()));
        transaction.setCategory(selectedCategory);
        transaction.setLocation(selectedLocation);
        transaction.setMonth(selectedMonth);
        transaction.setDate(tDate);
        transaction.setYear(tYear);
        transaction.setNotes(mTransactioNotes.getText().toString());
        String defaultValue = "null";
        PaymentMode paymentMode = new PaymentMode();
        paymentMode.setCardId(1);
        paymentMode.setCardNumber(1234);
        paymentMode.setCardType("Debit");
        String uid = sharedPref.getString("uid", defaultValue);
        transaction.setUid(uid);
/*        DatabaseReference mypostref = mDatabase.push();
        mypostref.setValue(transaction);
        Log.d("keyforfire",mypostref.getKey().toString());*/

        DatabaseReference mypostref = mDatabase.child(uid).child(String.valueOf(tYear)).child(selectedMonth).child("transactions").push();
        mypostref.keepSynced(true);
        mypostref.setValue(transaction);
        String txnId = mypostref.getKey();
        DatabaseReference mypostref1 = mDatabase.child(uid).child(String.valueOf(tYear)).child(selectedMonth).child("category").child(String.valueOf(selectedCategory.getId())).child(txnId).push();
        mypostref1.setValue(transaction);
        mypostref1.keepSynced(true);
        String categoryId = mypostref1.getKey();
        DatabaseReference mypostref2 = mDatabase.child(uid).child(String.valueOf(tYear)).child(selectedMonth).child("paymentMode").child(paymentMode.getCardType()).child(txnId).push();
        mypostref2.setValue(transaction);
        mypostref2.keepSynced(true);
        String paymentId = mypostref1.getKey();
        Log.d("keyfire", "txnId " + txnId + " categoryId " + categoryId + " paymentId " + paymentId);
    }



}
