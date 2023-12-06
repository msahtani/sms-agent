package com.smsagent.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Stack;
import java.util.function.Consumer;

public class SentReceiver extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {

        final Consumer<String> show = (msg) -> {
            Toast.makeText(
                    context,
                    msg,
                    Toast.LENGTH_LONG
            ).show();
        };

        switch(getResultCode()){
            case Activity.RESULT_OK:
                show.accept("sent successfully");
                break;
            case SmsManager.RESULT_CANCELLED:
                show.accept("cancelled");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                show.accept("please activate your mobile plan to send unlimited SMS xD");
                break;
            default:
                show.accept("i don't know what's this: " + getResultCode() );
                break;
        }

    }
}
