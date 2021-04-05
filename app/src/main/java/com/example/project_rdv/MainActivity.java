package com.example.project_rdv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myHelper;

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
                String addressItem = ((TextView)view.findViewById(R.id.place)).getText().toString();

                //String timeItem = ((TextView)view.findViewById(R.id.editTime)).getText().toString();
                String timeItem = "22:00";
                //String phoneItem = ((TextView)view.findViewById(R.id.editPhone)).getText().toString();
                String phoneItem = "0652297299";

                //RDV pRDV= new RDV(Long.parseLong(IdItem),titleItem,descItem,dateItem,timeItem,addressItem,phoneItem,false);
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
            case R.id.new_rdv:{
                Intent intent=new Intent(this,addRDV.class);
                startActivity(intent);
                return true;
            }
            case R.id.buttonParametre: {
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void chargeData(){
        final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE, DatabaseHelper.DESCRIPTION,DatabaseHelper.DATE,DatabaseHelper.TIME,DatabaseHelper.ADDRESS,
                DatabaseHelper.PHONE,DatabaseHelper.STATE};
        Log.v("----item-----", String.valueOf(from));
        final int[]to= new int[]{R.id.idRdv,R.id.title,R.id.comments,R.id.tvDate};

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
        return super.onContextItemSelected(item);
    }


}