package com.example.project_rdv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class DetailsRDV extends AppCompatActivity {
    private SharedPreferences sharedPreferencesStyle;
    private SharedPreferences sharedPreferencesLang;
    private boolean fromAdd;
    private DatabaseHelper myHelper;
    EditText eDate;
    EditText eTime;
    EditText eTel;
    EditText eTitle;
    EditText eDesc;
    EditText ePlace;
    TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesStyle = getSharedPreferences("PREF_THEME", Context.MODE_PRIVATE);
        int style = sharedPreferencesStyle.getInt("theme",R.style.Theme_AppCompat);
        setTheme(style);
        sharedPreferencesLang = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String language = sharedPreferencesLang.getString("lang","en");
        Locale locale = new Locale(language);
        Configuration conf = getResources().getConfiguration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_detailsrdv);

        tvId = (TextView) findViewById(R.id.displayId);
        eDesc  = (EditText) findViewById(R.id.displayDesc);
        ePlace =(EditText) findViewById(R.id.displayAddress);
        eDate=(EditText) findViewById(R.id.displayDate);
        eTime=(EditText) findViewById(R.id.displayTime);
        eTitle = (EditText) findViewById(R.id.displayTitle);
        eTel = (EditText) findViewById(R.id.displayPhone);

        myHelper = new DatabaseHelper(this);
        myHelper.open();

        Intent intent = getIntent();
        fromAdd = intent.getBooleanExtra("fromAdd", true);
        Log.v("--fromAdd--", String.valueOf(fromAdd));

        if(!fromAdd){
            Bundle b= intent.getExtras();
            RDV selectedRDV= b.getParcelable("SelectedRDV");
            tvId.setText(String.valueOf(selectedRDV.getId()));
            eTitle.setText(selectedRDV.getTitle());
            ePlace.setText(selectedRDV.getAddress());
            eDate.setText(selectedRDV.getDate());
            eTime.setText(selectedRDV.getTime());
            eTel.setText(selectedRDV.getPhone());
            eDesc.setText(selectedRDV.getDescription());

        }
    }
}
