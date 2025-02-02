# Learning images (Vision)

Shirates/Vision integrates machine learning tool(**CreateML**) in test running process. <br>
You can utilize the power of image classification in your test code. <br>
Just put template image files in labeled directories of classifiers.

## DefaultClassifier

**DefaultClassifier** is an image classifier for template matching.<br>
The name of parent directory of the image corresponds to the label.

![](_images/default_classifier.png)

A label directory has at least one child image file. Basically you may prepare one image file for one label.<br>
Label name is arbitrary, but we recommend `[Nickname]` like placeholder.

## CheckStateClassifier

**CheckStateClassifier** is for toggle widget states. This classifier has only two label (`[ON]` and `[OFF]`).

![](_images/check_state_classifier.png)

In above example, radio button and switch is supported. <br>
If you want to support checkbox state, append images into [ON]/[OFF] directory and run learning.

## Running learning

![](_images/running_learning.png)

To run learning explicitly

1. Right-click `CreateMlExecute` (`src/test/kotlin/batch/CreateMLExecute.kt`) and select `Debug 'CreateMLExecute'`.

**Note**<br>
You don't have to run learning explicitly. Learning is executed on demand when you start run your test.

### Console output

```
Connected to the target VM, address: '127.0.0.1:65452', transport: 'socket'
Copying jar content createml/MLImageClassifier.swift to /Users/wave1008/Downloads/TestResults/2025-01-28_133942
Copying jar content createml/MLImageClassifier.swift to /Users/wave1008/Downloads/TestResults/2025-01-28_133942
lineNo	[elapsedTime]	logDateTime	{testCaseId}	macroDepth	macroName	[logType]	timeDiff	mode	(group)	message
1	[00:00:00]	2025/01/28 13:39:42.978	{}	0	-	[info]	+0	C	()	Starting leaning. [CheckStateClassifier]
2	[00:00:01]	2025/01/28 13:39:44.361	{}	0	-	[info]	+1383	C	()	Learning completed. (in 1.340 sec)
["/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/CheckStateClassifier/MLImageClassifier.swift", "/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/CheckStateClassifier", "-noise", "-blur"]
----------------------------------
dataSourceName: CheckStateClassifier
dataSourcePath: file:///Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/CheckStateClassifier/
options: ["-noise", "-blur"]
featureExtractor: Image Feature Print V2
----------------------------------
Number of examples: 16
Number of classes: 2
Accuracy: 100.00%

******CONFUSION MATRIX******
----------------------------------
True\Pred [OFF] [ON]  
[OFF]     8     0     
[ON]      0     8     

******PRECISION RECALL******
----------------------------------
Class Precision(%) Recall(%)
[OFF] 100.00          100.00         
[ON]  100.00          100.00         


Model saved to /Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/CheckStateClassifier/CheckStateClassifier.mlmodel
3	[00:00:01]	2025/01/28 13:39:44.367	{}	0	-	[info]	+6	C	()	[learning [CheckStateClassifier]] in 1.631 sec
4	[00:00:01]	2025/01/28 13:39:44.368	{}	0	-	[info]	+1	C	()	Starting leaning. [DefaultClassifier]
5	[00:00:04]	2025/01/28 13:39:46.888	{}	0	-	[info]	+2520	C	()	Learning completed. (in 2.520 sec)
["/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/DefaultClassifier/MLImageClassifier.swift", "/Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/DefaultClassifier", "-noise", "-blur"]
----------------------------------
dataSourceName: DefaultClassifier
dataSourcePath: file:///Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/DefaultClassifier/
options: ["-noise", "-blur"]
featureExtractor: Image Feature Print V2
----------------------------------
Number of examples: 29
Number of classes: 28
Accuracy: 100.00%

******CONFUSION MATRIX******
**** (upperleft 20-by-20) ****
----------------------------------
True\Pred                                                                      @a[Android Files App][Images Button]                                           @a[Android Files App][This week Button]                                        @a[Android Settings App][Android Settings Top Screen][Apps Icon]               @a[Android Settings App][Android Settings Top Screen][Battery Icon]            @a[Android Settings App][Android Settings Top Screen][Connected devices Icon]  @a[Android Settings App][Android Settings Top Screen][Display Icon]            @a[Android Settings App][Android Settings Top Screen][Location Icon]           @a[Android Settings App][Android Settings Top Screen][Network & internet Icon] @a[Android Settings App][Android Settings Top Screen][Sound & vibration Icon]  @a[Android Settings App][Android Settings Top Screen][Storage Icon]            @a[Android Settings App][Android Settings Top Screen][System Icon]             @a[Android Settings App][Network & internet Screen][Airplane mode Icon]        @a[Android Settings App][Network & internet Screen][Calls & SMS Icon]          @a[Android Settings App][Network & internet Screen][Data Saver Icon]           @a[Android Settings App][Network & internet Screen][Hotspot & tethering Icon]  @a[Android Settings App][Network & internet Screen][Internet Icon]             @a[Android Settings App][Network & internet Screen][SIMs Icon]                 @a[Android Settings App][Network & internet Screen][VPN Icon]                  @a[misc][RadioButton(OFF)]                                                     @a[misc][RadioButton(ON)]                                                      
@a[Android Files App][Images Button]                                           1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Files App][This week Button]                                        0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Apps Icon]               0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Battery Icon]            0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Connected devices Icon]  0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Display Icon]            0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Location Icon]           0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Network & internet Icon] 0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Sound & vibration Icon]  0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][Storage Icon]            0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Android Settings Top Screen][System Icon]             0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][Airplane mode Icon]        0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][Calls & SMS Icon]          0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][Data Saver Icon]           0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][Hotspot & tethering Icon]  0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][Internet Icon]             0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][SIMs Icon]                 0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              0                                                                              
@a[Android Settings App][Network & internet Screen][VPN Icon]                  0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              0                                                                              
@a[misc][RadioButton(OFF)]                                                     0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              0                                                                              
@a[misc][RadioButton(ON)]                                                      0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              0                                                                              1                                                                              

******PRECISION RECALL******
----------------------------------
Class                                                                          Precision(%) Recall(%)
@a[Android Files App][Images Button]                                           100.00          100.00         
@a[Android Files App][This week Button]                                        100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Apps Icon]               100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Battery Icon]            100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Connected devices Icon]  100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Display Icon]            100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Location Icon]           100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Network & internet Icon] 100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Sound & vibration Icon]  100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][Storage Icon]            100.00          100.00         
@a[Android Settings App][Android Settings Top Screen][System Icon]             100.00          100.00         
@a[Android Settings App][Network & internet Screen][Airplane mode Icon]        100.00          100.00         
@a[Android Settings App][Network & internet Screen][Calls & SMS Icon]          100.00          100.00         
@a[Android Settings App][Network & internet Screen][Data Saver Icon]           100.00          100.00         
@a[Android Settings App][Network & internet Screen][Hotspot & tethering Icon]  100.00          100.00         
@a[Android Settings App][Network & internet Screen][Internet Icon]             100.00          100.00         
@a[Android Settings App][Network & internet Screen][SIMs Icon]                 100.00          100.00         
@a[Android Settings App][Network & internet Screen][VPN Icon]                  100.00          100.00         
@a[misc][RadioButton(OFF)]                                                     100.00          100.00         
@a[misc][RadioButton(ON)]                                                      100.00          100.00         
@a[misc][←]                                                                    100.00          100.00         
@i[iOS Settings App][Accessibility Icon]                                       100.00          100.00         
@i[iOS Settings App][Action Button Icon]                                       100.00          100.00         
@i[iOS Settings App][Apple Intelligence & Siri Icon]                           100.00          100.00         
@i[iOS Settings App][Camera Icon]                                              100.00          100.00         
@i[iOS Settings App][Developer Icon]                                           100.00          100.00         
@i[iOS Settings App][General Icon]                                             100.00          100.00         
@i[iOS Settings App][Screen Time Icon]                                         100.00          100.00         


Model saved to /Users/wave1008/github/ldi-github/shirates-core-vision-samples_en/build/vision/classifiers/DefaultClassifier/DefaultClassifier.mlmodel
6	[00:00:04]	2025/01/28 13:39:46.893	{}	0	-	[info]	+5	C	()	[learning [DefaultClassifier]] in 2.525 sec
Disconnected from the target VM, address: '127.0.0.1:65452', transport: 'socket'

Process finished with exit code 0
```

## build/vision/classifiers

Result files are output at `build/vision/classifiers`.

![](_images/build_vision_classifiers.png)

### CheckStateClassifier.mlmodel

This is the result mlmodel file.

### createML.log

This is the console log (see above **Console output**).

### MLImageClassifier.swift

This is the running script.

```swift
import CreateML
import Foundation

let arguments = CommandLine.arguments
print(arguments)
if arguments.count < 2 {
    print("path to data source is required")
    exit(1)
}
let options = arguments.filter { $0.starts(with: "-") }

var revision = 2
if let opt = options.first(where: { $0.starts(with: "-fp:")}) {
    if let r = Int(opt.components(separatedBy: [":","="]).last!) {
        revision = r
    }
}

let dataSourcePath = arguments[1]
let dataSourceURL = URL(fileURLWithPath: dataSourcePath)
let dataSourceName = dataSourceURL.lastPathComponent

print("----------------------------------")
print("dataSourceName: \(dataSourceName)")
print("dataSourcePath: \(dataSourceURL)")
print("options: \(options)")
print("featureExtractor: Image Feature Print V\(revision)")

// data source URLs
let trainingDataURL = dataSourceURL.appending(path: "training")
let testingDataURL = dataSourceURL.appending(path: "test")

// data sources
let trainingData = MLImageClassifier.DataSource.labeledDirectories(at: trainingDataURL)
let testingData = MLImageClassifier.DataSource.labeledDirectories(at: testingDataURL)

// model parameters
var augmentation = MLImageClassifier.ImageAugmentationOptions()
if options.contains("-noise") {
    augmentation.insert(.noise)
}
if options.contains("-blur") {
    augmentation.insert(.blur)
}
if options.contains("-crop") {
    augmentation.insert(.crop)
}
if options.contains("-exposure") {
    augmentation.insert(.exposure)
}
if options.contains("-flip") {
    augmentation.insert(.flip)
}
if options.contains("-rotation") {
    augmentation.insert(.rotation)
}
let modelParameters = MLImageClassifier.ModelParameters(
    validation: .split(strategy: .automatic),
    augmentation: augmentation,
    algorithm: .transferLearning(
        featureExtractor: .scenePrint(revision: revision),
        classifier: .logisticRegressor
    )
)

// training
let classifier = try MLImageClassifier(trainingData: trainingData, parameters: modelParameters)

// evaluation
let evaluation = classifier.evaluation(on: testingData)
print("\(evaluation)")

// save
var parent = dataSourceURL
parent.deleteLastPathComponent()
parent.append(path: "models")
let modelURL = URL(fileURLWithPath: "\(dataSourcePath)/\(dataSourceName).mlmodel")
try classifier.write(to: modelURL)

print("Model saved to \(modelURL.path)")
```

### Link

- [index](../../../index.md)
