package com.example.project_rdv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int CALL_PERMISSION_CODE = 1;
    private DatabaseHelper myHelper;
    SharedPreferences sharedPreferences;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHelper=new DatabaseHelper(this);
        myHelper.open();

        myListView = (ListView) findViewById(R.id.ListRdv);
        myListView.setEmptyView(findViewById(R.id.tvEmpty));

        chargeData();
        registerForContextMenu(myListView);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String IdItem= ((TextView)view.findViewById(R.id.idRdv)).getText().toString();
                String titleItem= ((TextView)view.findViewById(R.id.title)).getText().toString();
                String dateItem= ((TextView)view.findViewById(R.id.tvDate)).getText().toString();
                String descItem= ((TextView)view.findViewById(R.id.comments)).getText().toString();
                String addressItem = ((TextView)view.findViewById(R.id.address)).getText().toString();
                String timeItem = ((TextView)view.findViewById(R.id.time)).getText().toString();
                String phoneItem = ((TextView)view.findViewById(R.id.phone)).getText().toString();
                RDV pRDV= new RDV(Long.parseLong(IdItem),titleItem,descItem,dateItem,timeItem,addressItem,phoneItem,false);
                Intent intent = new Intent(getApplicationContext(), addRDV.class);
                intent.putExtra("SelectedRDV",pRDV);
                intent.putExtra("fromAdd",false);

                startActivity(intent);

            }
        });





        FloatingActionButton AddRDV = findViewById(R.id.AddRDV);
        AddRDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRDV = new Intent(getApplicationContext(),addRDV.class);
                startActivity(addRDV);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.rdv_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.buttonParametre: {
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void chargeData(){
        final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE, DatabaseHelper.DESCRIPTION,DatabaseHelper.DATE,DatabaseHelper.TIME,DatabaseHelper.ADDRESS,
                DatabaseHelper.PHONE,DatabaseHelper.STATE};

        final int[]to= new int[]{R.id.idRdv,R.id.title,R.id.comments,R.id.tvDate,R.id.time,R.id.address,R.id.phone,R.id.state};

        Cursor c = myHelper.getAllRDV();
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.rdv_item_view,c,from,to,0);
        adapter.notifyDataSetChanged();
        myListView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId()==R.id.delete){
            myHelper.delete(info.id);
            chargeData();
            return true;
        }
        if (item.getItemId()==R.id.localisation){
            Cursor c = myHelper.getAddress(info.id);

            String rdv_address = null;

            if (c.moveToFirst()){
                rdv_address = c.getString(c.getColumnIndex("address"));

            }

            String map = "http://maps.google.co.in/maps?q="+ String.valueOf(rdv_address) ;
            Uri gmmIntentUri = Uri.parse(map);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        }
        if (item.getItemId()==R.id.share){
            shareInfo(info.id);
        }
        if(item.getItemId()==R.id.call){
            Cursor c = myHelper.getPhone(info.id);
            String rdv_phone = null;
            if (c.moveToFirst()){
                rdv_phone = c.getString(c.getColumnIndex("phone"));
            }
            Log.v("---phone---",rdv_phone);
            if (checkCallPermission()){
                Log.v("---boucleCall---","hello");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + rdv_phone));
                startActivity(callIntent);
            }
            else {
                requestCallPermission();
            }

        }
        return super.onContextItemSelected(item);
    }

    private boolean checkCallPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestCallPermission(){
        String[] permission = {Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(this,permission,CALL_PERMISSION_CODE);
    }

    public void shareInfo(long id) {
        Cursor c = myHelper.getOneRDV(id);
        String info = null;
        if (c.moveToFirst()) {
            info = c.getString(c.getColumnIndex("title")) + " " +
                    c.getString(c.getColumnIndex("date")) + " " +
                    c.getString(c.getColumnIndex("time")) + " " +
                    c.getString(c.getColumnIndex("address"));
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, info);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    }