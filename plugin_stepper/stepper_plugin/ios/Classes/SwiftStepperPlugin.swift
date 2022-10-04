import Flutter
import UIKit
import CoreMotion

public class SwiftStepperPlugin: NSObject, FlutterPlugin {

    let pedoMeter = CMPedometer()
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "stepper_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftStepperPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if (call.method == "getPlatformVersion") {
     //   getStepsCount()

          getTodaysSteps{ stepCount in
                   result(String(stepCount))

                   }
          }
          else if (call.method == "getSteps") {
        /*        updateSteps {stepCount  in
                   result(String(format: "%.1f", stepCount))
              } */
              print("steps Count:" )
              getTodaysSteps{ stepCount in
              result(String(stepCount))

              }
              
              
          }else if(call.method == "permission"){

         isMotionPermissionGranted{ permission in
         result(String(permission))
         }


          
          }

  }

    func isMotionPermissionGranted(completion: @escaping (Bool) -> Void){
        let  isgranted = CMPedometer.authorizationStatus()
        if isgranted.rawValue == 3 {
            completion(Bool(truncating:true))
        }
        completion(Bool(truncating:false))
    }

    func getTodaysSteps(completion: @escaping (Int) -> Void) {

       /*  if isMotionPermissionGranted() { */

            if(CMPedometer.isStepCountingAvailable()){

                self.pedoMeter.queryPedometerData(from: Date().midnight, to: Date()) { (data : CMPedometerData!, error) -> Void in
                    if data != nil {
                        DispatchQueue.main.async {
                            print("steps Count:" ,data.numberOfSteps)
                            //                         self.steps.text = data.numberOfSteps.stringValue
                            completion(Int(truncating: data.numberOfSteps))
                        }
                    }
            /*     } */
            }

        }
    }


}

extension Date {
    func localString(dateStyle: DateFormatter.Style = .medium,
                     timeStyle: DateFormatter.Style = .medium) -> String {
        return DateFormatter.localizedString(
            from: self,
            dateStyle: dateStyle,
            timeStyle: timeStyle)
    }
    
    var midnight:Date{
        let cal = Calendar(identifier: .gregorian)
        return cal.startOfDay(for: self)
    }
    
}
