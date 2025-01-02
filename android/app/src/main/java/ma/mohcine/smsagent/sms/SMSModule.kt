package ma.mohcine.smsagent.sms

import android.Manifest
import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.UssdResponseCallback
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class SMSModule(private val context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {

    private var sentPI: PendingIntent? = null

    init {
        init()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun init() {
        val sentReceiver = SentReceiver()

        context.registerReceiver(
            sentReceiver,
            IntentFilter("SENT")
        )

        sentPI = PendingIntent.getBroadcast(
            context,
            0,
            Intent("SENT"),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    suspend fun sendUssdRequestAsync() = suspendCancellableCoroutine { continuation ->

        val callback = object : UssdResponseCallback() {
            override fun onReceiveUssdResponse(telephonyManager: TelephonyManager, request: String, response: CharSequence) {
                Log.d("USSD", "USSD Response: $response")
                continuation.resume(response.toString())
            }

            override fun onReceiveUssdResponseFailed(telephonyManager: TelephonyManager, request: String, failureCode: Int) {
                Log.e("USSD", "USSD Request failed with code: $failureCode")
                continuation.resumeWithException(Exception("USSD Request failed with code: $failureCode"))
            }
        }

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.sendUssdRequest("#99#", callback, null);

    }

    private fun getSmsManager(): SmsManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
            // For Android 12 and above
            // val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            val subscriptionId = SubscriptionManager.getDefaultSmsSubscriptionId()
            SmsManager.getSmsManagerForSubscriptionId(subscriptionId)
        } else {
            // For Android 11 and below
            SmsManager.getDefault()
        }
    }


    @ReactMethod
    fun getPhoneNumber(promise: Promise) {
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val phoneNumber = sendUssdRequestAsync()
                promise.resolve(phoneNumber)
                Log.d("USSD", "USSD Response: $phoneNumber")
            } catch (e: Exception) {
                Log.e("USSD", "Error: ${e.message}")
                promise.reject("USSD", "Error: ${e.message}")
            }
        }
    }

    @ReactMethod
    fun send(phoneNumber: String, message: String) {

        val smsManager = getSmsManager()

        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            sentPI,
            null
        )

        // TODO : test 404 http


        val payload: WritableMap = Arguments.createMap()
        payload.putString("key", "value")

        context
            .getJSModule(RCTDeviceEventEmitter::class.java)
            .emit("sendSms", payload)
    }



    override fun getName(): String = "SmsModule"
    
}



