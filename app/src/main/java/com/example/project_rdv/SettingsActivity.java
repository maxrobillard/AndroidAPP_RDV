package com.example.project_rdv;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("lang","en");
        setContentView(R.layout.activity_settings);

        switch(language){

            case "en":
                RadioButton rbEnglish = findViewById(R.id.rbEnglish);
                rbEnglish.setChecked(true);

                break;

            case "fr":
                RadioButton rbFrench = findViewById(R.id.rbFrench);
                rbFrench.setChecked(true);
                break;
        }



    }


    public void onLanguageRadioButtonClick(View view){

        boolean checked = ((RadioButton) view).isChecked();
        editor = sharedPreferences.edit();
        String language = "en";
        switch(view.getId()){
            case R.id.rbEnglish:
                if (checked) {
                    language = "en";
                    editor.putString("lang", "en");

                    editor.commit();
                }
                break;
            case R.id.rbFrench:
                language = "fr";
                if (checked){
                    editor.putString("lang", "fr");
                    editor.commit();
                }

                break;

        }

        Locale locale = new Locale(language);
        Configuration conf = getResources().getConfiguration();

        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf,
                getBaseContext().getResources().getDisplayMetrics());

        //finish();
        //startActivity(getIntent());
    }
    public void onBackClick(View v){
        //Intent intent = new Intent(this,MainActivity.class);
        // startActivity(intent);
        finish();
    }
}
