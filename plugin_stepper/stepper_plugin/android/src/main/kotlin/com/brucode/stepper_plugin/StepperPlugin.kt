package com.brucode.stepper_plugin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.os.Build
import androidx.annotation.NonNull
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContextWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.brucode.stepper_plugin_example.callback.stepsCallback
import com.brucode.stepper_plugin.helper.PrefsHelper
import com.brucode.stepper_plugin.service.StepDetectorService
import com.brucode.stepper_plugin.service.DefaultClass
import com.brucode.stepper_plugin.service.Restarter

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** StepperPlugin */
class StepperPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, stepsCallback, PluginRegistry.RequestPermissionsResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  lateinit var activity: Activity
  private var _flutterBinding: FlutterPluginBinding? = null
  val REQUEST_WRITE_PERMISSION = 100
  private var permissionGranted: Boolean = false

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPluginBinding) {
    _flutterBinding = flutterPluginBinding
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "stepper_plugin")
  //  registrar.addRequestPermissionsResultListener(this)

    Log.e("activity---->","initialize here......")
 //   activity = flutterPluginBinding.applicationContext as Activity

  }



  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    channel.setMethodCallHandler(this)

    PrefsHelper.Builder()
      .setContext(activity)
      .setMode(ContextWrapper.MODE_PRIVATE)
      .setPrefsName(activity.packageName)
      .setUseDefaultSharedPreference(true)
      .build();

    createNotificationChannel()
    binding.addRequestPermissionsResultListener(this)

/*
    permissionGranted = ContextCompat.checkSelfPermission(activity.applicationContext,
      android.Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    if (!permissionGranted ) {
      ActivityCompat.requestPermissions(
        activity,
        arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
        REQUEST_WRITE_PERMISSION
      )
    }


    val intent = Intent(activity, StepDetectorService::class.java)
    activity.startService(intent)*/

    permissionGranted = ContextCompat.checkSelfPermission(activity.applicationContext,
      android.Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    if (!permissionGranted ) {
      ActivityCompat.requestPermissions(activity,
        arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
        REQUEST_WRITE_PERMISSION )
    }


    Log.d("TAG", "onAttachedToActivity: Call onAttachedToActivity ---> $activity")
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    permissionGranted = ContextCompat.checkSelfPermission(activity.applicationContext,
      android.Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    binding.addRequestPermissionsResultListener(this)
  }

  override fun onDetachedFromActivity() {
    val broadcastIntent = Intent()
    broadcastIntent.setAction("restartservice")
    broadcastIntent.setClass(activity, Restarter::class.java)
    activity.sendBroadcast(broadcastIntent)
  }


  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val serviceChannel = NotificationChannel(
        "CHANNEL_ID",
        "Contact Tracing Service",
        NotificationManager.IMPORTANCE_DEFAULT
      )
      val manager = activity.getSystemService(
        NotificationManager::class.java
      )
      manager.createNotificationChannel(serviceChannel)
    }
  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    Log.e("plugin---->","${call.method}")
    permissionGranted = ContextCompat.checkSelfPermission(activity.applicationContext,
      android.Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    if (call.method == "getPlatformVersion") {
      Log.e("permission---->","${permissionGranted}")
      if(permissionGranted){
        val intent = Intent(activity, StepDetectorService::class.java)
        activity.startService(intent)
        result.success("Steps Count Start")
      }else{
        result.success("Please add permission")
      }

    } else if(call.method == "getSteps") {
      Log.e("permission---->","${permissionGranted}")
      if(permissionGranted){
        var defaultClass =
          DefaultClass()
        defaultClass.RETURN_STEPS = getSteps()

        result.success(defaultClass.RETURN_STEPS)
      }else{
        result.success("Please add permission firstly, then you avaialble to get steps.")
      }

    } else {
      result.notImplemented()
    }


  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }




  override fun subscribeSteps(steps: Int) {
    Log.e("getSteps------>","" +  steps.toString())
  }

  fun getSteps(): String{

    return  PrefsHelper.getInt("FSteps").toString()
  }

  override fun onRequestPermissionsResult( requestCode: Int,
                                           permissions: Array<out String>?,
                                           grantResults: IntArray?): Boolean {

    when (requestCode) {
      REQUEST_WRITE_PERMISSION -> {
        if ( null != grantResults ) {
          permissionGranted = grantResults.isNotEmpty() &&
                  grantResults.get(0) == PackageManager.PERMISSION_GRANTED
        }

        // do something here to handle the permission state

        channel.setMethodCallHandler(this)
        Log.e("permission retrieve","here  ${permissionGranted}")

        // only return true if handling the request code
        return true
      }
    }
    return false
  }

}
