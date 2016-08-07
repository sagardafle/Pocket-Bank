package com.pocketbank.lazylad91.pocketbank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.pocketbank.lazylad91.pocketbank.Services.ZoomOutPageTransformer;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FirebaseAuth mAuth;
    private static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //code for checking login if not send to login page
        Log.d("checkforLogin()",checkforLogin().toString());
        if(!checkforLogin()){
            Intent intent = new Intent();
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        if (checkforLogin() && checkforIntro()) {
            Intent intent = new Intent();
            Intent i = new Intent(HomeActivity.this, Intro.class);
            startActivity(i);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent launchaddtransaction = new Intent(HomeActivity.this, AddTransactionActivity.class);
                startActivity(launchaddtransaction);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* code for tabbed activity */
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
/*        tabLayout.addTab(tabLayout.newTab().setText("List").setIcon(R.mipmap.iconrecy));
        tabLayout.addTab(tabLayout.newTab().setText("Card View").setIcon(R.mipmap.iconcard));*/
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabLayout.setupWithViewPager(mViewPager);
        /* code for tabbed activity */

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageView);
        TextView displayemailid = (TextView)hView.findViewById(R.id.emailtextView);


        String defaultValue = "null";

        String uid = sharedPref.getString("photo", defaultValue);
        String imgurl = sharedPref.getString("url",defaultValue);
        String useremailid = sharedPref.getString("email",defaultValue);

        Log.d("imgurl", imgurl);
        Log.d("emailid", useremailid);


        displayemailid.setText(useremailid);

        Glide
                .with(getApplicationContext())
                .load(imgurl)
                .override(100, 50)
                .crossFade()
                .into(imgvw);





    }

    private boolean checkforIntro() {
        if(sharedPref==null){
            Context context = getApplicationContext();
            sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        }
        Boolean defaultValue = true;
        Boolean isIntroActive = sharedPref.getBoolean(getString(R.string.intro), defaultValue);
        Log.d("isIntroActive",isIntroActive.toString());
        return isIntroActive;
    }

    private Boolean checkforLogin() {
        // Creating shared preferences code start
        Context context = getApplicationContext();
         sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // creating shared preferences code stop
       // SharedPreferences sharedPref = HomeActivity.this.getPreferences(Context.MODE_PRIVATE);
        Boolean defaultValue = false;
        Boolean isloggedIn = sharedPref.getBoolean(getString(R.string.loggedInKey), defaultValue);
        Log.d("isLoggedIn",isloggedIn.toString());
        return isloggedIn;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            addTransaction();
        } else if (id == R.id.nav_gallery) {

            addBudgets();

        } else if (id == R.id.nav_slideshow) {
            exportPDF();

        }
//        else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        }
        else if (id == R.id.nav_send) {
                logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addBudgets() {
        Intent intent = new Intent(HomeActivity.this, BudgetActivity.class);
        startActivity(intent);
        //finish();
    }

private void exportPDF() {
        Intent intent = new Intent(HomeActivity.this, PrintTransaction.class);
        startActivity(intent);
        //finish();
    }


    private void addTransaction() {
        Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
        startActivity(intent);
        //finish();
    }

    private void logout() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.loggedInKey), false);
        editor.commit();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    // Code for tabbed activity start

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_plus_one, container, false);
            /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 1:
                    return new homeFragment();
                case 2:
                    return new transactionlist();
                case 3:
                    return new chartFragment();
                default:
                    return new chartFragment();
            }
           // return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Analytics";
                case 1:
                    return "Dashboard";
                case 2:
                    return "Transactions";
            }
            return null;
        }
    }
    // code for tabbed activity ended


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test","Test");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

                    }
                    return;
                } else {
                    // Permission Denied
                    //Toast.makeText(HomeActivity.this, "Application Wont work", Toast.LENGTH_SHORT)
                    //.show();
                    return;
                    /*if (mMap != null)
                        onMapReady(mMap);*/
                }
            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test","Test");
                        return;
                } else {
                    // Permission Denied
                    //Toast.makeText(HomeActivity.this, "Application Wont work", Toast.LENGTH_SHORT)
                    //.show();
                    return;
                    /*if (mMap != null)
                        onMapReady(mMap);*/
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
