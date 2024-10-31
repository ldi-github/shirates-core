//
// vncommand
//
// for macOS

import Foundation
import AppKit
import Vision

extension NSImage {
    var cgImage: CGImage? {
        guard let data = self.tiffRepresentation else { return nil }
        guard let source = CGImageSourceCreateWithData(data as CFData, nil) else { return nil }
        return CGImageSourceCreateImageAtIndex(source, 0, nil)
    }
}

extension [String] {
    func string(name: String) -> String? {

        guard let value = self.first(where: { $0.starts(with: "\(name):") }) else {
            return nil
        }

        let tokens = value.components(separatedBy: ":")
        guard tokens.count == 2 else {
            return nil
        }
        return tokens[1]
    }
    func int(name: String) -> Int? {

        guard let value = self.string(name: name) else {
            return nil
        }
        return Int(value)
    }
    func float(name: String) -> Float? {

        guard let value = self.string(name: name) else {
            return nil
        }
        return Float(value)
    }
    func double(name: String) -> Double? {

        guard let value = self.string(name: name) else {
            return nil
        }
        return Double(value)
    }
}

struct RecognizeTextResult: Encodable {
    struct Item : Encodable {
        let text: String
        let confidence: Float
        let rect: String
    }

    let imagePath: String
    var items : [Item] = []
}


func recognizeText(
    argv: [String]
) -> String {

    guard argv.count>=3 else {
        printUsage()
        exit(1)
    }

    let imagePath = argv[2]
    let outputPath = if argv.count>=4 && !argv[3].contains(":") { argv[3] } else { "" }
    let language = argv.string(name: "language")

    let nsImage = NSImage(contentsOfFile: imagePath)
    let cgImage = nsImage!.cgImage!
    var resultString = "?"

    let request = VNRecognizeTextRequest { (request, error) in
        guard let observations = request.results as? [VNRecognizedTextObservation] else {
            return
        }
        resultString = getJsonString(observations: observations)
    }
    if language != nil {
        request.recognitionLanguages.insert(language!, at: 0)
    }

    let handler = VNImageRequestHandler(cgImage: cgImage, options: [:])
    try? handler.perform([request])
    print(resultString)
    if !outputPath.isEmpty {
        try! resultString.write(toFile: outputPath, atomically: true, encoding: .utf8)
    }
    return resultString

    func getJsonString(observations: [VNRecognizedTextObservation]) -> String {

        var result = RecognizeTextResult(imagePath: imagePath)
        let size = CGSize(width: cgImage.width, height: cgImage.height)
        for o in observations {
            let candidate = o.topCandidates(1).first!
            let text = candidate.string
            let confidence = candidate.confidence
            let b = o.boundingBox
            let x = String(format: "%.1f", b.origin.x * size.width)
            let y = String(format: "%.1f", (1 - b.origin.y) * size.height)
            let width = String(format: "%.1f", b.size.width * size.width)
            let height = String(format: "%.1f", b.size.height * size.height)
            let rect = "[\(x), \(y), \(width), \(height)]"

            let item = RecognizeTextResult.Item(text: text, confidence: confidence, rect: rect)
            result.items.append(item)
        }

        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let data = try! encoder.encode(result)
        let jsonString = String(data: data, encoding: .utf8)!

        return jsonString
    }

}

struct DetectRectanglesResult: Encodable {
    struct Item : Encodable {
        let confidence: Float
        let rect: String
    }

    let imagePath: String
    let maximumObservations: Int
    let minimumSize: Float
    let minimumAspectRatio: Float
    let maximumAspectRatio: Float
    var items : [Item] = []
}

func detectRectangles(
    argv: [String]
) -> String {

    guard argv.count>=3 else {
        printUsage()
        exit(1)
    }

    let imagePath = argv[2]
    let outputPath = if argv.count>=4 && !argv[3].contains(":") { argv[3] } else { "" }
    let maximumObservations: Int = argv.int(name: "maximumObservations") ?? 100
    let minimumSize: Float = argv.float(name: "minimumSize") ?? 0.1
    let minimumAspectRatio: Float = argv.float(name: "minimumAspectRatio") ?? 0.01
    let maximumAspectRatio: Float = argv.float(name: "minimumAspectRatio") ?? 1

    let nsImage = NSImage(contentsOfFile: imagePath)!
    let cgImage = nsImage.cgImage!
    var resultString = "?"

    let request = VNDetectRectanglesRequest { (request, error) in
        guard let observations = request.results as? [VNRectangleObservation] else {
            return
        }
        resultString = getJsonString(observations: observations)
    }
    request.maximumObservations = maximumObservations
    request.minimumSize = minimumSize
    request.minimumAspectRatio = minimumAspectRatio
    request.maximumAspectRatio = maximumAspectRatio

    let handler = VNImageRequestHandler(cgImage: cgImage, options: [:])
    try? handler.perform([request])
    print(resultString)
    if !outputPath.isEmpty {
        try! resultString.write(toFile: outputPath, atomically: true, encoding: .utf8)
    }
    return resultString

    func getJsonString(observations: [VNRectangleObservation]) -> String {

        var result = DetectRectanglesResult(
            imagePath: imagePath,
            maximumObservations: maximumObservations,
            minimumSize: minimumSize,
            minimumAspectRatio: minimumAspectRatio,
            maximumAspectRatio: maximumAspectRatio
        )
        let size = CGSize(width: cgImage.width, height: cgImage.height)
        for o in observations {
            let confidence = o.confidence
            let x = String(format: "%.1f", o.topLeft.x * size.width)
            let y = String(format: "%.1f", o.topLeft.y * size.height)
            let width = String(format: "%.1f", o.boundingBox.width * size.width)
            let height = String(format: "%.1f", o.boundingBox.height * size.height)
            let rect = "[\(x), \(y), \(width), \(height)]"

            let item = DetectRectanglesResult.Item(confidence: confidence, rect: rect)
            result.items.append(item)
        }

        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let data = try! encoder.encode(result)
        let jsonString = String(data: data, encoding: .utf8)!

        return jsonString
    }

}

func printUsage() {
    print()
    print("usage:")
    print("vncommand recognize-text <image-path> <output-path>")
    print("vncommand detect-rectangles <image-path> <output-path>")
    print()
}

//recognizeText(argv: [
//    "",
//    "recognize-text",
//    "/Users/wave1008/dev/vision-eval-app/VisionEvalApp/Assets.xcassets/DownloadDialog.imageset/DownloadDialog.png",
//    "/Users/wave1008/Downloads/DownloadDialog_detect_recognize-text.json"]
//)

//recognizeText(argv: [
//    "/Users/wave1008/github/ldi-github/shirates-core/unitTestData/files/vision/android_settings.png",
//    "/Users/wave1008/Downloads/android_settings_recognize-text.json"]
//)
//

//detectRectangles(argv: [
//    "",
//    "detect-rectangles",
//    "/Users/wave1008/dev/vision-eval-app/VisionEvalApp/Assets.xcassets/DownloadDialog.imageset/DownloadDialog.png",
//    "/Users/wave1008/Downloads/DownloadDialog_detect-rectangles.json",
////    "maximumObservations:2"
//]
//)





let argv = ProcessInfo.processInfo.arguments


let subCommand = argv[1]
let imagePath = argv[2]

switch (subCommand) {
case "recognize-text":
    recognizeText(argv: argv)
case "detect-rectangles":
    detectRectangles(argv: argv)
default:
    printUsage()
    exit(1)
}
