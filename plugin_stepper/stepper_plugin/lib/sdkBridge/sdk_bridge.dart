
import 'package:stepper_plugin/stepper_plugin.dart';
import 'package:stepper_plugin/stepMethodKeys.dart';
import 'package:eventify/eventify.dart';

class SdkBridge {
  static SdkBridge? instance;
  EventEmitter eventEmitter = EventEmitter();

  StepperPlugin stepperPlugin = StepperPlugin();

  ///Singleton object creation
  static SdkBridge getInstance(context) {
    if (instance == null) {
      instance = SdkBridge();
    }
    return instance!;
  }

  ///Callbacks registration frm constructor
  SdkBridge() {
    callStepperCallBack();
  }

  ///Method to register all the RDNA callbacks
  callStepperCallBack() {
    stepperPlugin.on(StepMethodKeys.SDK_INITIALIZE, onInitializedStepper);
    stepperPlugin.on(StepMethodKeys.GET_STEPS, getStepsCount);
    stepperPlugin.on(StepMethodKeys.GET_PERMISSION, getPermission);


  }

  ///plugin initialization
  Future<String> onInitializedStepper() async {
    String initializeSdk = "";
    initializeSdk =  await   stepperPlugin.platformVersion().then((response) {
      print('Response status: ${response}');
      return response;
    },onError: (error){
      print('Response error: ${error.toString()}');
      return error.toString();
    }).catchError((error) {
      print("Error: $error");
      return error;
    });

    return initializeSdk;
  }

  ///get counter step
  Future<String> getStepsCount() async{
    print("call");
    String countSteps = "";
    countSteps = await  stepperPlugin.countSteps().then((response) {
      print('Response status: ${response}');
      return response;
    },onError: (error){
      print('Response error: ${error.toString()}');
      return error.toString();
    }).catchError((error) {
      print("Error: $error");
      return error;
    });

    return countSteps;

  }
  Future<String>getPermission()async{
    String permission="";
    permission = await stepperPlugin.getPermission().then((response) {
      return response.toString();
    },
      onError: (error){
      print(error.toString());
      return error.toString();

      }
    ).catchError((error){
      print(error.toString());
      return error.toString();
    });
    return permission.toString();
  }


}