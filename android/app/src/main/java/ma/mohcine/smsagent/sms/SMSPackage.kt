package ma.mohcine.smsagent.sms

import androidx.annotation.NonNull
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ReactShadowNode

import android.view.View


class SMSPackage : ReactPackage {


    override fun createViewManagers(
        reactContext: ReactApplicationContext
    ): MutableList<ViewManager<View, ReactShadowNode<*>>> = mutableListOf()

    override fun createNativeModules(
        reactContext: ReactApplicationContext
    ): MutableList<NativeModule> = listOf(SMSModule(reactContext)).toMutableList()
}

