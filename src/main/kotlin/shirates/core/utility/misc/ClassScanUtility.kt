package shirates.core.utility.misc

import org.junit.platform.commons.util.ReflectionUtils
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Path
import kotlin.reflect.KClass

object ClassScanUtility {

    /**
     * findAnnotatedClasses
     */
    fun <T : Annotation> findAnnotatedClasses(
        scanDir: Path,
        targetAnnotation: KClass<T>
    ): MutableList<KClass<*>> {
        val files = File(scanDir.toUri()).walkTopDown()
            .filter { it.isFile }.toList()
        val packageNameMap = mutableMapOf<String, Int>()

        val srcTestKotlin = "src/test/kotlin".toPath().toString()
        val srcTestJava = "src/test/java".toPath().toString()

        for (file in files) {
            val packageName = file.toPath().parent.toString()
                .replace(srcTestKotlin, "")
                .replace(srcTestJava, "")
                .removePrefix(File.separator)
                .replace(File.separator, ".")

            packageNameMap[packageName] = 1
        }

        val targetClassMap = mutableMapOf<KClass<*>, Int>()
        for (packageName in packageNameMap.keys) {
            val list = ReflectionUtils.findAllClassesInPackage(
                packageName,
                { c -> c.isAnnotationPresent(targetAnnotation.java) },
                { _ -> true })
                .map { it.kotlin }
            for (c in list) {
                targetClassMap[c] = 1
            }
        }
        return targetClassMap.keys.toMutableList()
    }

//    /**
//     * findClasses
//     */
//    inline fun <reified T : KClass<*>> findClasses(
//        scanPath: Path,
//    ): MutableList<KClass<*>> {
//        val files = File(scanPath.toUri()).walkTopDown()
//            .filter { it.isFile }.toList()
//        val packageNameMap = mutableMapOf<String, Int>()
//
//        val srcTestKotlin = "src/test/kotlin".toPath().toString()
//        val srcTestJava = "src/test/java".toPath().toString()
//
//        for (file in files) {
//            val packageName = file.toPath().parent.toString()
//                .replace(srcTestKotlin, "")
//                .replace(srcTestJava, "")
//                .removePrefix(File.separator)
//                .replace(File.separator, ".")
//
//            packageNameMap[packageName] = 1
//        }
//
//        val targetClassMap = mutableMapOf<KClass<*>, Int>()
//        for (packageName in packageNameMap.keys) {
//            val list = ReflectionUtils.findAllClassesInPackage(
//                packageName,
//                { c -> c is T },
//                { _ -> true })
//                .map { it.kotlin }
//            for (c in list) {
//                targetClassMap[c] = 1
//            }
//        }
//        return targetClassMap.keys.toMutableList()
//    }

}