
import 'dart:async';

import 'package:flutter/services.dart';

class StepperPlugin {
  static const MethodChannel _channel = MethodChannel('stepper_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static Future<String?> get countSteps async {
    String? return_steps = "";
    await _channel.invokeMethod('getSteps').then((value) {
      if(value != null){
        print("pluginSteps---> $value");
        return_steps = value;
      }
    },onError: (error){
      return error.toString();
    });

    print("returnCountSteps---> ${_channel.invokeMethod('getSteps')}");
    return return_steps;
  }
}
