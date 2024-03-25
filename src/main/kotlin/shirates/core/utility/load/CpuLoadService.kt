package shirates.core.utility.load

import com.sun.management.OperatingSystemMXBean
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.Message.message
import java.lang.management.ManagementFactory
import java.util.*
import kotlin.concurrent.thread

object CpuLoadService {

    class LoadRecord(
        val number: Int,
        val date: Date,
        val cpuLoad: Double
    )

    enum class CpuLoadServiceState {
        None,
        InService,
        Stopping,
        Stopped
    }

    var intervalMilliseconds = Const.CPU_LOAD_INTERVAL_MILLISECONDS
    var maxHistories = Const.CPU_LOAD_MAX_HISTORIES
    val loadRecords = mutableListOf<LoadRecord>()
    var thread: Thread? = null
    var serviceState = CpuLoadServiceState.None
    var printDebug = false
    var cpuLoadForSafety = Const.CPU_LOAD_FOR_SAFETY
    var countOnAverage = Const.COUNT_ON_AVERAGE

    private fun printDebug(message: String) {

        if (printDebug) {
            println(message)
        }
    }

    internal fun getCpuLoad(): Double {

        val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        var cpuLoad = osBean.getCpuLoad() * 100
        if (cpuLoad == 0.0 || cpuLoad.isNaN()) {
            Thread.sleep(1)
            cpuLoad = osBean.getCpuLoad() * 100
        }
        return cpuLoad
    }

    private fun addRecord() {

        val lastRecordNumber = loadRecords.lastOrNull()?.number ?: 0
        val cpuLoad = getCpuLoad()
        if (cpuLoad.isNaN()) {
            return
        }
        val record = LoadRecord(number = lastRecordNumber + 1, date = Date(), cpuLoad = cpuLoad)
        if (loadRecords.count() == maxHistories) {
            loadRecords.removeAt(0)
        }
        loadRecords.add(record)
        if (printDebug) {
            val average = getAverageCpuLoad(countOnAverage)
            println("cpuLoad: " + "%.2f".format(cpuLoad) + ", average: " + "%.2f".format(average))
        }
    }

    /**
     * startService
     */
    fun startService(
        cpuLoadForSafety: Int? = null,
        countOnAverage: Int? = null,
        printDebug: Boolean = PropertiesManager.enableWaitCpuLoadPrintDebug
    ) {
        if (cpuLoadForSafety != null) {
            this.cpuLoadForSafety = cpuLoadForSafety
        }
        if (countOnAverage != null) {
            this.countOnAverage = countOnAverage
        }
        this.printDebug = printDebug

        printDebug("CpuLoadService.startService()")

        loadRecords.clear()

        if (thread != null && serviceState == CpuLoadServiceState.InService) {
            stopService()
        }
        serviceState = CpuLoadServiceState.InService
        thread = thread(start = true) {
            while (true) {
                if (serviceState == CpuLoadServiceState.Stopping) {
                    this.thread = null
                    serviceState = CpuLoadServiceState.Stopped
                    break
                }

                addRecord()
                Thread.sleep(intervalMilliseconds)
            }
        }
    }

    /**
     * stopService
     */
    fun stopService() {

        if (thread == null) {
            return
        }

        printDebug("CpuLoadService.stopService()")

        serviceState = CpuLoadServiceState.Stopping
        for (i in 1..5) {
            if (serviceState == CpuLoadServiceState.Stopped) {
                break
            }
            Thread.sleep(intervalMilliseconds)
        }
    }

    /**
     * getLastCpuLoad
     */
    fun getLastCpuLoad(): Double {

        if (loadRecords.isEmpty()) {
            addRecord()
        }
        return loadRecords.last().cpuLoad
    }

    /**
     * getAverageCpuLoad
     */
    fun getAverageCpuLoad(countOnAverage: Int = this.countOnAverage): Double {

        val records = loadRecords.takeLast(countOnAverage)
        if (records.isEmpty()) {
            return 0.0
        }
        val average = records.map { it.cpuLoad }.average()
        return average
    }

    /**
     * waitForCpuLoadUnder
     */
    fun waitForCpuLoadUnder(percentage: Int = cpuLoadForSafety) {

        if (percentage < 0 || percentage > 100) {
            throw IllegalArgumentException(message(id = "cpuLoadForSafety", value = percentage.toString()))
        }
        if (thread == null) {
            return
        }
        if (PropertiesManager.enableWaitCpuLoad.not()) {
            return
        }

        printDebug("CpuLoadService.waitForCpuLoadUnder($percentage)")

        while (true) {
            val lastCpuLoad = getLastCpuLoad()
            if (lastCpuLoad < percentage) {
                val average = getAverageCpuLoad()
                if (average < percentage) {
                    break
                }
            }
            Thread.sleep(this.intervalMilliseconds)
        }
    }

}