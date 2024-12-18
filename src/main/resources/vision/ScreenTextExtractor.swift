//

import Foundation
import Vision
import AppKit
import ImageIO
import CoreGraphics
import UniformTypeIdentifiers


func getCGImage(path: String) -> CGImage {

    let imageUrl = URL(fileURLWithPath: path)
    let image = CGImageSourceCreateImageAtIndex(CGImageSourceCreateWithURL(imageUrl as CFURL, nil)!,0, nil)!
    return image
}

struct ExecutionError: Error, CustomStringConvertible {
    var description: String
    var error: Error?

    init(_ description: String, error : Error? = nil) {
        self.description = description
        self.error = error
    }
}

func getAllFilesRecursively(at path: String) throws -> [String] {
    let fileManager = FileManager.default
    var allFiles: [String] = []

    do {
        let contents = try fileManager.subpathsOfDirectory(atPath: path)
        for content in contents {
            if (content.hasSuffix(".jpg") || content.hasSuffix(".png")){
                let fullPath = (path as NSString).appendingPathComponent(content)
                var isDirectory: ObjCBool = false
                if fileManager.fileExists(atPath: fullPath, isDirectory: &isDirectory), !isDirectory.boolValue {
                    allFiles.append(fullPath)
                }
            }
        }
    } catch {
        throw ExecutionError("Error while enumerating files. (inputDirectory=\(inputDirectory), \(error.localizedDescription))")
    }

    return allFiles
}

func recognizeTextOf(inputImageFile: String, language: String?) async throws -> [String] {

    let image = getCGImage(path: inputImageFile)

    var request = RecognizeTextRequest()
    if(language != nil){
        request.recognitionLanguages[0] = Locale.Language(identifier: language!)
    }
    let result = try await request.perform(on: image)

    var list: [String] = []

    for r in result {
        let candidate = r.topCandidates(1).first!
        let text = candidate.string
        list.append(text)
    }
    return list
}


let arguments = CommandLine.arguments

let inputDirectory = arguments[1]
let language = arguments.count > 2 ? arguments[2] : nil

if(!FileManager.default.fileExists(atPath: inputDirectory)){
    throw ExecutionError("inputDirectory not found. (\(inputDirectory))")
}

let inputImageFiles = try getAllFilesRecursively(at: inputDirectory)

var dictionary: [String: [String]] = [:]

for inputImageFile in inputImageFiles {
    let recognizedText = try await recognizeTextOf(inputImageFile: inputImageFile, language: language)
    let name = (inputImageFile as NSString).deletingPathExtension.components(separatedBy: "/").last ?? ""
    dictionary[name] = recognizedText
}

let jsonData = try JSONSerialization.data(withJSONObject: dictionary, options: .prettyPrinted)
let jsonString = String(data: jsonData, encoding: .utf8) ?? ""

print(jsonString)

