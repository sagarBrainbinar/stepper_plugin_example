#import "StepperPlugin.h"
#if __has_include(<stepper_plugin/stepper_plugin-Swift.h>)
#import <stepper_plugin/stepper_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "stepper_plugin-Swift.h"
#endif

@implementation StepperPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftStepperPlugin registerWithRegistrar:registrar];
}
@end
