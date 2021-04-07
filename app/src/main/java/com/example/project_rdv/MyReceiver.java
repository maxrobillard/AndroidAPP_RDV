package com.example.project_rdv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, MyNewIntentService.class);
        myIntent.putExtra("RDVTitle",intent.getStringExtra("RDVTitle"));
        context.startForegroundService(myIntent);
    }
}
