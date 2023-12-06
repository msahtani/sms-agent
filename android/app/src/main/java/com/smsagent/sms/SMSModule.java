package com.smsagent.sms;

import static android.content.Context.RECEIVER_EXPORTED;
import static android.content.Context.RECEIVER_VISIBLE_TO_INSTANT_APPS;

import static com.facebook.react.modules.core.DeviceEventManagerModule.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;


public class SMSModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext context;

    private PendingIntent sentPI;

    public SMSModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
        init();
    }

    

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void init(){
        BroadcastReceiver sentReceiver = new SentReceiver();

        context.registerReceiver(
                sentReceiver,
                new IntentFilter("SENT")
        );

        sentPI = PendingIntent.getBroadcast(
                context,
                0,
                new Intent("SENT"),
                PendingIntent.FLAG_IMMUTABLE
        );
    }


    @ReactMethod
    public void send(String phoneNumber, String message){

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                phoneNumber,
                null,
                message,
                sentPI,
                null
        );

        // TODO : test 404 http

        WritableMap payload = Arguments.createMap();
        payload.putString("key", "value");

        context.getJSModule(
                RCTDeviceEventEmitter.class
        ).emit("sendSms", payload);

    }



    @NonNull
    @Override
    public String getName() {
        return "SmsModule";
    }
}