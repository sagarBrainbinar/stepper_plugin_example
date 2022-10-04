import 'dart:js';

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:stepper_plugin/sdkBridge/sdk_bridge.dart';

void main() {
  const MethodChannel channel = MethodChannel('stepper_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    SdkBridge  bridge = SdkBridge.getInstance(context);
    expect(await bridge.onInitializedStepper(), '42');
  });
}
