package com.pocketbank.lazylad91.pocketbank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public final class Intro extends AppIntro {

    private static SharedPreferences sharedPref;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
       /* addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);
*/
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.intro), false);
        editor.commit();

        addSlide(AppIntroFragment.newInstance("Sign in with Google", "One click and you are sign in", R.drawable.googles, R.color.white));
        addSlide(AppIntroFragment.newInstance("Add Expenses", "Add Expenses on your ease now", R.drawable.introt, R.color.white));
        addSlide(AppIntroFragment.newInstance("Setup your Budget", "Now keep track of your savings", R.drawable.intro, R.color.white));
        // OPTIONAL METHODS

        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices
        setNavBarColor(R.color.colorPrimary);

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
       /* setVibrate(true);
        setVibrateIntensity(30);*/
        setSwipeLock(false);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR
        /*setZoomAnimation(); // OR
        setFlowAnimation(); // OR
        setSlideOverAnimation(); // OR
        setDepthAnimation(); // OR
        *//*setZoomAnimation(); // OR
        setFlowAnimation(); // OR
        setSlideOverAnimation(); // OR
        setDepthAnimation(); // OR
        *///setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);

    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        Intent intent = new Intent();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onNextPressed() {

        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}
