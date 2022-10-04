package com.brucode.stepper_plugin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.brucode.stepper_plugin.callback.stepsCallback
import com.brucode.stepper_plugin.helper.PrefsHelper
import com.brucode.stepper_plugin.service.DefaultClass
import com.brucode.stepper_plugin.service.StepDetectorService

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** StepperPlugin */
class StepperPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, stepsCallback {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  lateinit var activity: Activity
  private var _flutterBinding: FlutterPluginBinding? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPluginBinding) {
    _flutterBinding = flutterPluginBinding
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "stepper_plugin")


    Log.e("activity---->","initialize here......")
 //   activity = flutterPluginBinding.applicationContext as Activity

  }



  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    channel.setMethodCallHandler(this)
    Log.d("TAG", "onAttachedToActivity: Call onAttachedToActivity ---> $activity")
  }

  override fun onDetachedFromActivityForConfigChanges() {

  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {

  }

  override fun onDetachedFromActivity() {

  }


  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    Log.e("plugin---->","${call.method}")
    if (call.method == "getPlatformVersion") {
      Log.e("plugin---->","start here......")
      if(ContextCompat.checkSelfPermission(activity.applicationContext,android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 1)
      }else{

        val intent = Intent(activity, StepDetectorService::class.java)
        activity.startService(intent)
        result.success("Steps Count Start")
      }
    } else if(call.method == "getSteps") {

      var defaultClass = DefaultClass()
      defaultClass.RETURN_STEPS = getSteps()

      result.success(defaultClass.RETURN_STEPS)

    }else{
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


}
