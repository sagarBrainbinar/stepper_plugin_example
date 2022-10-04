import Flutter
import UIKit
import HealthKit

public class SwiftStepperPlugin: NSObject, FlutterPlugin {

    let healthStore = HKHealthStore()
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "stepper_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftStepperPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if (call.method == "getPlatformVersion") {
        getStepsCount()
          }
          else if (call.method == "getSteps") {
               updateSteps {stepCount  in
                   result(String(format: "%.1f", stepCount))
              }
              
              
          }
  }


    func getStepsCount() {
        // Access Step Count
        let healthKitTypes: Set = [ HKObjectType.quantityType(forIdentifier: HKQuantityTypeIdentifier.stepCount)! ]
        // Check for Authorization
        healthStore.requestAuthorization(toShare: healthKitTypes, read: healthKitTypes) { (success, error) in
            if (success) {
              /*  self.updateSteps { stepCount in
                    "return_steps"
                }*/

                print("health permission provided")
            } else {
                print("health permission not provided")
            }
        }
    }


    func updateSteps(completion: @escaping (Double) -> Void) {
        let stepsQuantityType = HKQuantityType.quantityType(forIdentifier: .stepCount)!

        let now = Date()
        let startOfDay = Calendar.current.startOfDay(for: now)
        let predicate = HKQuery.predicateForSamples(withStart: startOfDay, end: now, options: .strictStartDate)

        let query = HKStatisticsQuery(quantityType: stepsQuantityType, quantitySamplePredicate: predicate, options: .cumulativeSum) { (_, result, error) in
            var resultCount = 0.0

            guard let result = result else {
                print("\(String(describing: error?.localizedDescription)) ")
                completion(resultCount)
                return
            }

            if let sum = result.sumQuantity() {
                resultCount = sum.doubleValue(for: HKUnit.count())
            }

            DispatchQueue.main.async {
                completion(resultCount)
            }
        }

        healthStore.execute(query)
    }

}

