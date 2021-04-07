package com.example.project_rdv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class DetailsRDV extends AppCompatActivity {
    private SharedPreferences sharedPreferencesStyle;
    private SharedPreferences sharedPreferencesLang;
    private boolean fromAdd;
    private DatabaseHelper myHelper;
    TextView eDate;
    TextView eTime;
    TextView eTel;
    TextView eTitle;
    TextView eDesc;
    TextView ePlace;
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
        eDesc  = (TextView) findViewById(R.id.displayDesc);
        ePlace =(TextView) findViewById(R.id.displayAddress);
        eDate=(TextView) findViewById(R.id.displayDate);
        eTime=(TextView) findViewById(R.id.displayTime);
        eTitle = (TextView) findViewById(R.id.displayTitle);
        eTel = (TextView) findViewById(R.id.displayPhone);

        myHelper = new DatabaseHelper(this);
        myHelper.open();

        Intent intent = getIntent();
        fromAdd = intent.getBooleanExtra("fromAdd", true);
        Log.v("--fromAdd--", String.valueOf(fromAdd));
        Bundle b= intent.getExtras();
        RDV selectedRDV= b.getParcelable("SelectedRDV");

        if(!fromAdd){
            tvId.setText(String.valueOf(selectedRDV.getId()));
            eTitle.setText(selectedRDV.getTitle());
            ePlace.setText("     "+selectedRDV.getAddress());
            eDate.setText(selectedRDV.getDate());
            eTime.setText(selectedRDV.getTime());
            eTel.setText("     "+selectedRDV.getPhone());
            eDesc.setText(selectedRDV.getDescription());

        }

        FloatingActionButton modifyRDV = findViewById(R.id.Modifyrdv);
        modifyRDV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.v("------", String.valueOf(tvId));
                String IdItem= String.valueOf(selectedRDV.getId());
                String titleItem= selectedRDV.getTitle();
                String dateItem= selectedRDV.getDate();
                String descItem= selectedRDV.getDescription();
                String addressItem = selectedRDV.getAddress();
                String timeItem = selectedRDV.getTime();
                String phoneItem = selectedRDV.getPhone();
                RDV pRDV= new RDV(Long.parseLong(IdItem),titleItem,descItem,dateItem,timeItem,addressItem,phoneItem,false);
                Intent intent = new Intent(getApplicationContext(), addRDV.class);
                intent.putExtra("SelectedRDV",pRDV);
                intent.putExtra("fromAdd",false);
                startActivity(intent);


            }
        });
    }
    public void onCancelClick2(View v){
        //Intent intent = new Intent(this,MainActivity.class);
        // startActivity(intent);
        finish();
    }
}
