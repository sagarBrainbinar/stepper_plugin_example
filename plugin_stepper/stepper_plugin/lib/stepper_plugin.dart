import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:eventify/eventify.dart';
import 'package:stepper_plugin/stepMethodKeys.dart';

class StepperPlugin {

  factory StepperPlugin() {
    if (_instance == null) {
      const MethodChannel methodChannel =
      MethodChannel("stepper_plugin");
      final EventEmitter eventEmitter = EventEmitter();
      _instance =
          StepperPlugin.private(methodChannel, eventEmitter);
      // print('CALL PLUGIN AGAIN');
    }
    return _instance!;
  }


  StepperPlugin.private(this._channel, this._eventEmitter);

  static StepperPlugin? _instance;
  EventEmitter _eventEmitter;
  final MethodChannel _channel;


  /// Registers event listeners for SdkBridge events
  void on(String event, Function handler) {
    EventCallback cb = (event, cont) {
      handler(event.eventData);
    };
    _eventEmitter.on(event, null, cb);
  }

  /// Clears all event listeners
  void clear() {
    _eventEmitter.clear();
  }

  Future<String> platformVersion() async {
    String version = await _channel.invokeMethod(StepMethodKeys.SDK_INITIALIZE);

    debugPrint("return_version $version");

    return version;
  }

  Future<String> countSteps() async {
    debugPrint("return_steps");
    String returnSteps = await _channel.invokeMethod(StepMethodKeys.GET_STEPS);

    debugPrint("return_steps123 $returnSteps");

    return returnSteps;
  }
  Future<String> getPermission()async{
    debugPrint("getting permission");
    String permission = await _channel.invokeMethod(StepMethodKeys.GET_PERMISSION);
    return permission;
  }

}
