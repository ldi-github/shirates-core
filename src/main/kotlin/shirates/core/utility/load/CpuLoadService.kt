package shirates.core.utility.load

import com.sun.management.OperatingSystemMXBean
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
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
    var printDebug = PropertiesManager.enableWaitCpuLoadPrintDebug
    var safeCpuLoad = PropertiesManager.safeCpuLoad

    private fun printDebug(message: String) {

        if (printDebug) {
            TestLog.info(message)
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
            printDebug("cpuLoad: " + "%.2f".format(cpuLoad))
        }
    }

    /**
     * startService
     */
    fun startService(
        safeCpuLoad: Int? = null,
        printDebug: Boolean? = null
    ) {
        if (safeCpuLoad != null) {
            this.safeCpuLoad = safeCpuLoad
        }
        if (printDebug != null) {
            this.printDebug = printDebug
        }

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
     * getCpuLoadExpectation
     */
    fun getCpuLoadExpectation(): Double {

        val records = loadRecords.takeLast(2)
        if (records.isEmpty()) {
            return 0.0
        }
        if (records.count() == 1) {
            return records[0].cpuLoad
        }
        val diff = records[0].cpuLoad - records[1].cpuLoad

        var expectation = records[1].cpuLoad + diff
        if (expectation < 0.0) {
            expectation = 0.0
        } else if (expectation > 100.0) {
            expectation = 100.0
        }
        return expectation
    }

    /**
     * waitForCpuLoadUnder
     */
    fun waitForCpuLoadUnder(safeCpuLoad: Int = this.safeCpuLoad) {

        if (safeCpuLoad < 0 || safeCpuLoad > 100) {
            throw IllegalArgumentException(message(id = "safeCpuLoad", value = safeCpuLoad.toString()))
        }
        if (thread == null) {
            return
        }
        if (PropertiesManager.enableWaitCpuLoad.not()) {
            return
        }

        printDebug("CpuLoadService.waitForCpuLoadUnder($safeCpuLoad)")

        while (true) {
            val lastCpuLoad = getLastCpuLoad()
            if (lastCpuLoad < safeCpuLoad) {
                val expectation = getCpuLoadExpectation()
                if (expectation < safeCpuLoad) {
                    break
                }
            }
            Thread.sleep(this.intervalMilliseconds)
        }
    }

}