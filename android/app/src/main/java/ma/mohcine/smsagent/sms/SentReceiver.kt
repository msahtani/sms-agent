package ma.mohcine.smsagent.sms

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi

class SentReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent) {

        val show: (String) -> Unit = { msg ->
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_LONG
            ).show()
        }

        when (resultCode) {
            Activity.RESULT_OK -> show("sent successfully")
            SmsManager.RESULT_CANCELLED -> show("cancelled")
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> show("please activate your mobile plan to send unlimited SMS xD")
            else -> show("i don't know what's this: $resultCode")
        }
    }
}

