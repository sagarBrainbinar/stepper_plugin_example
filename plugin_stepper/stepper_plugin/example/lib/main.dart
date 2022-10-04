import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'dart:async';
import 'package:get/get.dart';
import 'package:flutter/services.dart';
import 'package:stepper_plugin/sdkBridge/sdk_bridge.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String getSteps = "getSteps";
  String? stepCount;

  SdkBridge? bridge;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.

  Future<void> initPlatformState() async {
    String platformVersion,countSteps;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      bridge = SdkBridge.getInstance(context);
      platformVersion = await bridge!.onInitializedStepper() ;

      countSteps =await bridge!.getStepsCount();
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
      countSteps = "0";
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      getSteps = countSteps;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin Example'),
        ),
        body: Container(
          width: Get.width,
          height: Get.height,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Running on: $_platformVersion\n'),
              const SizedBox(height: 20,),
              InkWell(
                  onTap: () async{
                    String? getSteps = await bridge!.getStepsCount();

                    setState(() {
                      stepCount= getSteps;
                    });

                    print("steps----> $getSteps,,,, $stepCount");
                    Fluttertoast.showToast(
                        msg: "$stepCount",
                        toastLength: Toast.LENGTH_SHORT,
                        gravity: ToastGravity.BOTTOM,
                        timeInSecForIosWeb: 1,
                        backgroundColor: Colors.black54,
                        textColor: Colors.white,
                        fontSize: 16.0
                    );

                  },
                  child: Container(
                    width: Get.width,
                    height: 40,
                    margin: const EdgeInsets.symmetric(horizontal: 50),

                    decoration: const BoxDecoration(
                      color: Colors.lightBlue,
                      borderRadius: BorderRadius.all(Radius.circular(10)),

                    ),
                    child:const Center(
                      child:   Text(
                        'Get  Steps',
                        style: TextStyle(
                            color: Colors.white
                        ),
                      ),
                    ),
                  )
              ),
              Text('currentStep on: $stepCount\n'),
            ],
          ),
        ),
      ),
    );
  }
}
