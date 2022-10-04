#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "StepperPlugin.h"

FOUNDATION_EXPORT double stepper_pluginVersionNumber;
FOUNDATION_EXPORT const unsigned char stepper_pluginVersionString[];

