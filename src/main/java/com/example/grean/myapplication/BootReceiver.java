package com.example.grean.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * To start app after device boot completed..
 */
public class BootReceiver extends BroadcastReceiver {
    static final String broadcast = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(broadcast)){
            Intent i = new Intent(context, Main.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
