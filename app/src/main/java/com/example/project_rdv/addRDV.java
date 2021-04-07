package com.example.project_rdv;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class addRDV extends AppCompatActivity {
    DatabaseHelper myHelper;
    DatePickerDialog picker;
    TimePickerDialog picker1;
    EditText eDate;
    EditText eTime;
    EditText eTel;
    EditText eTitle;
    EditText eDesc;
    EditText ePlace;
    TextView tvId;
    private static final int CONTACT_PERMISSION_CODE = 1;
    private static  final int CONTACT_PICK_CODE = 2;
    private boolean fromAdd;
    SharedPreferences sharedPreferencesStyle;
    SharedPreferences sharedPreferencesLang;


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

        setContentView(R.layout.activity_addrdv);


        Button accueil = findViewById(R.id.buttonAccueil);
        ImageButton AccessContact = findViewById(R.id.AccessContact);
        Button submit = findViewById(R.id.Submission);

        tvId = (TextView) findViewById(R.id.editId);
        eDesc  = (EditText) findViewById(R.id.editDesc);
        ePlace =(EditText) findViewById(R.id.editAddress);
        eDate=(EditText) findViewById(R.id.editDate);
        eTime=(EditText) findViewById(R.id.editTime);
        eTitle = (EditText) findViewById(R.id.editTitle);
        eTel = (EditText) findViewById(R.id.editPhone);

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

        AccessContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkContactPermission()){
                    pickContactIntent();
                }
                else {
                    requestContactPermission();
                }
            }
        });

        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker1 = new TimePickerDialog(addRDV.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String Hour = String.format("%02d", sHour);
                                String Minute = String.format("%02d", sMinute);
                                eTime.setText(Hour + ":" + Minute);
                            }
                        }, hour, minutes, true);
                picker1.show();
            }
        });


        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(addRDV.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthYear = String.format("%02d", monthOfYear+1);
                                String dayMonth = String.format("%02d", dayOfMonth);
                                eDate.setText(dayMonth + "/" + (monthYear) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


    }

    public void saveRdv(View v) {
        String title= eTitle.getText().toString();
        String date=eDate.getText().toString();
        String phone = eTel.getText().toString();
        String description = eDesc.getText().toString();
        String time = eTime.getText().toString();
        String address = ePlace.getText().toString();

        Intent main;

        if(fromAdd){
            RDV rdv = new RDV(title, description, date, time, address, phone, false);
            myHelper.add(rdv);
            main = new Intent(v.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }
        else {
            Long id = Long.parseLong(tvId.getText().toString());
            RDV moment = new RDV(id,title, description, date, time, address, phone, false);
            int n = myHelper.update(moment);

            main = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }




        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NOTIF", Context.MODE_PRIVATE);
        String reminder = sharedPreferences.getString("reminder","1w");
        int nbWeeks = 1;
        switch (reminder){
            case "1w":
                nbWeeks =1 ;
                break;
            case "2w":
                nbWeeks =2 ;
                break;
            case "3w":
                nbWeeks =3 ;
                break;
        }
        try {

            final String OLD_FORMAT = "MM-dd-yyyy";
            final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
            String newDateString;
            SimpleDateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
            Date d = formatter.parse(date);
            formatter.applyPattern(NEW_FORMAT);
            newDateString = formatter.format(d);

            Timestamp ts = Timestamp.valueOf(newDateString);
            long tsTime= ts.getTime();

            Intent notifyIntent = new Intent(this,MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP,  tsTime - TimeUnit.MINUTES.toMillis(nbWeeks*7*24*60), pendingIntent);

        } catch(Exception e) {

        }
        startActivity(main);

    }

    public void onCancelClick(View v){
        //Intent intent = new Intent(this,MainActivity.class);
        // startActivity(intent);
        finish();
    }

    private boolean checkContactPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestContactPermission(){
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this,permission,CONTACT_PERMISSION_CODE);
    }
    private void pickContactIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickContactIntent();
            }
            else {
                Toast.makeText(this,"Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int ResultCode,@NonNull Intent data){
        super.onActivityResult(requestCode,ResultCode,data);
        if (ResultCode == RESULT_OK){
            if(requestCode == CONTACT_PICK_CODE){
                eTel.setText("");
                Cursor cursor1, cursor2;

                Uri uri = data.getData();
                cursor1 = getContentResolver().query(uri, null,null,null,null);
                if (cursor1.moveToFirst()){
                    String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);

                    if(idResultHold == 1) {
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null,
                                null
                        );
                        while (cursor2.moveToNext()) {
                            String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            eTel.setText(contactNumber);

                        }
                        cursor2.close();
                    }
                    cursor1.close();
                }

            }
        }
        else{

        }
    }
}
