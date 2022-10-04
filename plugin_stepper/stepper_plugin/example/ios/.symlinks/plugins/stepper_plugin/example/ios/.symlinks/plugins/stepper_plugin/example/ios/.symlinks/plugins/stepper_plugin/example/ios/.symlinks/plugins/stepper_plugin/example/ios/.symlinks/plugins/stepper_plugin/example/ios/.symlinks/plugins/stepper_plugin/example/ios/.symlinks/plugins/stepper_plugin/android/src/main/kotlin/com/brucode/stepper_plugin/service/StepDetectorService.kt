package com.brucode.stepper_plugin.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.brucode.stepper_plugin.helper.GeneralHelper
import com.brucode.stepper_plugin.helper.PrefsHelper
import kotlin.math.roundToInt

class StepDetectorService : Service(), SensorEventListener{

    var defaultClass = DefaultClass()


  /*  companion object {
        lateinit var callback: stepsCallback
    }*/

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(countSensor != null){
            Toast.makeText(this, "Step Detecting Start", Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL)

            GeneralHelper.updateNotification(this, this, PrefsHelper.getInt("FSteps"))



            defaultClass.STEPS_TAKE = PrefsHelper.getInt("FSteps")
           // callback.subscribeSteps(defaultClass.STEPS_TAKE)

        }else{
            Toast.makeText(this, "Sensor Not Detected", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (PrefsHelper.getString("TodayDate") != GeneralHelper.getToadyDate()) {
            PrefsHelper.putInt("Steps", p0!!.values[0].roundToInt())
            PrefsHelper.putString("TodayDate", GeneralHelper.getToadyDate())
            defaultClass.STEPS_TAKE = 0
            Toast.makeText(this,  "todayDate:" + GeneralHelper.getToadyDate(), Toast.LENGTH_SHORT).show()
        } else {
            val storeSteps = PrefsHelper.getInt("Steps")
            val sensorSteps = p0!!.values[0].roundToInt()
            val finalSteps = sensorSteps - storeSteps
            if (finalSteps > 0) {
                PrefsHelper.putInt("FSteps", finalSteps)
                GeneralHelper.updateNotification(this, this, finalSteps)
                defaultClass.STEPS_TAKE = finalSteps
               // callback.subscribeSteps(defaultClass.STEPS_TAKE)
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("SERVICE", p0.toString())
    }



    /*   object subscribe {
           fun register(activity: Activity) {
               Log.d("activityPackageName", activity.packageName)
               callback = activity as stepsCallback
           }
       }*/

}