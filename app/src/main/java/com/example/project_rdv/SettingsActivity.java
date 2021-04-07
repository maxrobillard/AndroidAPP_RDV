package com.example.project_rdv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesLang;
    SharedPreferences sharedPreferencesStyle;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferencesReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferencesLang = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        String language = sharedPreferencesLang.getString("lang", "en");

        sharedPreferencesReminder = getSharedPreferences("PREF_NOTIF", Context.MODE_PRIVATE);
        String reminder = sharedPreferencesReminder.getString("reminder","1w");
        Log.v("------reminder-----",reminder);
        sharedPreferencesStyle = getSharedPreferences("PREF_THEME", Context.MODE_PRIVATE);
        int style = sharedPreferencesStyle.getInt("theme", R.style.Theme_AppCompat);
        setTheme(style);
        setContentView(R.layout.activity_settings);

        switch (language) {

            case "en":
                RadioButton rbEnglish = findViewById(R.id.rbEnglish);
                rbEnglish.setChecked(true);

                break;

            case "fr":
                RadioButton rbFrench = findViewById(R.id.rbFrench);
                rbFrench.setChecked(true);
                break;
        }

        switch (style) {

            case R.style.Theme_AppCompat_Light:
                RadioButton rbLight = findViewById(R.id.rbLight);
                rbLight.setChecked(true);

                break;

            case R.style.Theme_AppCompat:
                RadioButton rbDark = findViewById(R.id.rbDark);
                rbDark.setChecked(true);
                break;

            case R.style.AppTheme_Green:
                RadioButton rbGreen = findViewById(R.id.rbGreen);
                rbGreen.setChecked(true);
                break;

            case R.style.AppTheme_Purple:
                RadioButton rbPurple = findViewById(R.id.rbPurple);
                rbPurple.setChecked(true);
                break;
        }
        switch (reminder){

            case "1w":
                RadioButton rbOneWeek = findViewById(R.id.rbOneWeek);
                rbOneWeek.setChecked(true);
                break;

            case "2w":

                RadioButton rbTwoWeek = findViewById(R.id.rbTwoWeek);
                rbTwoWeek.setChecked(true);
                break;

            case "3w":

                RadioButton rbThreeWeek = findViewById(R.id.rbThreeWeek);
                rbThreeWeek.setChecked(true);
                break;

        }


    }


    public void onLanguageRadioButtonClick(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        editor = sharedPreferencesLang.edit();
        String language = "en";
        switch (view.getId()) {
            case R.id.rbEnglish:
                if (checked) {
                    language = "en";
                    editor.putString("lang", "en");
                    editor.commit();
                    editor.apply();
                }
                break;
            case R.id.rbFrench:
                language = "fr";
                if (checked) {
                    editor.putString("lang", "fr");
                    editor.commit();
                    editor.apply();
                }

                break;


        }

        Locale locale = new Locale(language);
        Configuration conf = getResources().getConfiguration();

        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();

        //finish();
        //startActivity(getIntent());
    }

    public void onStyleRadioButtonClick(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        editor = sharedPreferencesStyle.edit();
        int style = R.style.Theme_AppCompat;
        switch (view.getId()) {
            case R.id.rbLight:
                if (checked) {
                    style = R.style.Theme_AppCompat_Light;
                    editor.putInt("theme", style);
                    editor.commit();
                    editor.apply();
                }
                break;
            case R.id.rbDark:
                style = R.style.Theme_AppCompat;
                if (checked) {
                    editor.putInt("theme", style);
                    editor.commit();
                    editor.apply();
                }

                break;

            case R.id.rbGreen:
                style = R.style.AppTheme_Green;
                if (checked) {
                    editor.putInt("theme", style);
                    editor.commit();
                    editor.apply();
                }

                break;

            case R.id.rbPurple:
                style = R.style.AppTheme_Purple;
                if (checked) {
                    editor.putInt("theme", style);
                    editor.commit();
                    editor.apply();
                }

                break;

        }
        changeTheme(style);
    }

    public void onBackClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    public void changeTheme(int newTheme) {
        this.setTheme(newTheme);
        recreate();
    }

    public void onReminderRadioButtonClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        editor = sharedPreferencesReminder.edit();


        switch (view.getId()) {

            case R.id.rbOneWeek:
                if (checked) {
                    Log.v("---1---","is checked");
                    editor.putString("reminder", "1w");
                    editor.commit();
                    editor.apply();
                }
                break;

            case R.id.rbTwoWeek:
                if (checked) {
                    Log.v("---2---","is checked");
                    editor.putString("reminder", "2w");
                    editor.commit();
                    editor.apply();
                }
                break;

            case R.id.rbThreeWeek:
                if (checked) {
                    Log.v("---3---","is checked");
                    editor.putString("reminder", "3w");
                    editor.commit();
                    editor.apply();
                }
                break;

        }

    }
}
