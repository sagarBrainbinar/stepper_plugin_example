package com.brucode.stepper_plugin.service

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.os.Build
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.Context
import com.brucode.stepper_plugin.service.StepDetectorService


class Restarter : BroadcastReceiver() {

   override fun onReceive(context: Context, intent: Intent?) {
        Log.i("Broadcast Listened", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, StepDetectorService::class.java))
        } else {
            context.startService(Intent(context, StepDetectorService::class.java))
        }
    }
}